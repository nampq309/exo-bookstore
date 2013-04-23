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

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;

@ComponentConfig(
 lifecycle = UIFormLifecycle.class
)
public class BookstoreForm extends UIForm {
  
  
  /**
   * Listens to edit event and changes the form to edit mode.<br>
   *
   */
  public static class EditActionListener extends EventListener<BookstoreForm> {

    @Override
    public void execute(Event<BookstoreForm> event) throws Exception {
      
    }
  }
  
  /**
   * Listens to save event and change form to non edit mode.<br> 
   *
   */
  public static class SaveActionListener extends EventListener<BookstoreForm> {

    @Override
    public void execute(Event<BookstoreForm> event) throws Exception {

    }
  }
  
  /**
   * Listens to cancel event and change the form to non edit mode.<br>
   *
   */
  public static class CancelActionListener extends EventListener<BookstoreForm> {

    @Override
    public void execute(Event<BookstoreForm> event) throws Exception {

    }
  }

}
