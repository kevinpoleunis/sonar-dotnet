/*
 * Sonar .NET Plugin :: Gendarme
 * Copyright (C) 2010 Jose Chillan, Alexandre Victoor and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.csharp.gendarme;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.platform.ServerFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the Gendarme rules configuration file.
 */
public class GendarmeRuleRepository extends RuleRepository {

  private String repositoryKey;
  private ServerFileSystem fileSystem;
  private XMLRuleParser xmlRuleParser;
  private Settings settings;

  public GendarmeRuleRepository(String repoKey, String languageKey, ServerFileSystem fileSystem, XMLRuleParser xmlRuleParser,
      Settings settings) {
    super(repoKey, languageKey);
    setName(GendarmeConstants.REPOSITORY_NAME);
    this.fileSystem = fileSystem;
    this.xmlRuleParser = xmlRuleParser;
    this.repositoryKey = repoKey;
    this.settings = settings;
  }

  @Override
  public List<Rule> createRules() {
    List<Rule> rules = new ArrayList<Rule>();

    // Gendarme rules
    rules.addAll(xmlRuleParser.parse(GendarmeRuleRepository.class.getResourceAsStream("/org/sonar/plugins/csharp/gendarme/rules/rules.xml")));

    // Custom rules:
    // - old fashion: XML files in the file system
    for (File userExtensionXml : fileSystem.getExtensions(repositoryKey, "xml")) {
      rules.addAll(xmlRuleParser.parse(userExtensionXml));
    }
    // - new fashion: through the Web interface
    String customRules = settings.getString(GendarmeRuleRepositoryProvider.SONAR_GENDARME_CUSTOM_RULES_PROP_KEY);
    if (StringUtils.isNotBlank(customRules)) {
      rules.addAll(xmlRuleParser.parse(new StringReader(customRules)));
    }

    return rules;
  }
}
