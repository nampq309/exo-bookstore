/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.bookstore;

import javax.portlet.PortletMode;

import org.exoplatform.bookstore.webui.UIBookstoreContainer;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

@ComponentConfig(lifecycle = UIApplicationLifecycle.class, 
  template = "app:/groovy/webui/portlet/UIBookStoreMainPortlet.gtmpl"
)
public class UIBookStoreMainPortlet extends UIPortletApplication {

  /**
   * Constructor of Main Portlet
   * 
   * @throws Exception
   */
  public UIBookStoreMainPortlet() throws Exception {

  }

  @Override
  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    PortletRequestContext portletReqContext = (PortletRequestContext) context;
    if (portletReqContext.getApplicationMode().equals(PortletMode.VIEW)) {
      if (getChild(UIBookstoreContainer.class) == null) {
        addChild(UIBookstoreContainer.class, null, null);
      }
    }
    super.processRender(app, context);
  }

}
