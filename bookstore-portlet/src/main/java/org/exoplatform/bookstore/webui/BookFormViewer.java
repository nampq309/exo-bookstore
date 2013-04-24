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
package org.exoplatform.bookstore.webui;

import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

@ComponentConfig(
 events = {
   @EventConfig(listeners = BookFormViewer.CloseActionListener.class)
 } 
)
public class BookFormViewer extends UIContainer {
  
  public BookFormViewer() throws Exception {
    
  }
  
  /**
   * Listens to close the form view
   */
  public static class CloseActionListener extends EventListener<BookFormViewer> {

    @Override
    public void execute(Event<BookFormViewer> event) throws Exception {
      
    }
  }

}
