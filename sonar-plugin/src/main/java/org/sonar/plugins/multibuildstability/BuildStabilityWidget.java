/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonar.plugins.buildstability;

import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.NavigationSection;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.UserRole;

/**
 * @author Evgeny Mandrikov
 */
@NavigationSection(NavigationSection.RESOURCE)
@UserRole(UserRole.USER)
public class BuildStabilityWidget extends AbstractRubyTemplate implements RubyRailsWidget {
  public String getId() {
    return "buildstability-widget";
  }

  public String getTitle() {
    return "BuildStability widget";
  }

  @Override
  protected String getTemplatePath() {
    return "/org/sonar/plugins/buildstability/buildStabilityWidget.erb";
  }
}
