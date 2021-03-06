/*
 * Sonar C# Plugin :: C# Squid :: Squid
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
package com.sonar.csharp.squid.parser.rules.expressions;

import com.sonar.csharp.squid.parser.CSharpGrammar;
import com.sonar.csharp.squid.parser.RuleTest;
import org.junit.Before;
import org.junit.Test;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExpressionTest extends RuleTest {

  @Before
  public void init() {
    p.setRootRule(p.getGrammar().rule(CSharpGrammar.EXPRESSION));
  }

  @Test
  public void reallife() {
    assertThat(p)
        .matches("CurrentDomain.GetAssemblies()")
        .matches("dbCommand.Dispose()")
        .matches("p.field++.ToString()")
        .matches("this.Id++")
        .matches("a++.ToString().ToString()")
        .matches("int.Parse(\"42\")")
        .matches("int.Parse(\"42\").ToString()")
        .matches("int.MaxValue")
        .matches("new []{12, 13}")
        .matches("new []{12, 13}.ToString()")
        .matches("new[] { 12, 13 }.Length")
        .matches("new[] { 12, 13 }[0]")
        .matches("db.Users")
        .matches("new { name }")
        .matches("new { name, foo }")
        .matches("new { user.Name, user.Role.Name }")
        .matches("from user in db.Users select new { user.Name, RoleName = user.Role.Name }")
        .matches("new int?(42)")
        .matches("null")
        .matches("false ? new int?(42) : null");
  }

}
