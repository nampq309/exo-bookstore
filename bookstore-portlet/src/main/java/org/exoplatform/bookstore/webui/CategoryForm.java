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

import org.exoplatform.bookstore.BookstoreUtils;
import org.exoplatform.bookstore.jcr.model.Category;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatform.webui.form.validator.SpecialCharacterValidator;
import org.exoplatform.webui.form.validator.StringLengthValidator;

@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template = "system:/groovy/webui/form/UIForm.gtmpl",
  events = {
    @EventConfig(listeners = CategoryForm.SaveActionListener.class),
    @EventConfig(listeners = CategoryForm.ResetActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = CategoryForm.CancelActionListener.class, phase = Phase.DECODE)
  } 
)
public class CategoryForm extends UIForm {
	
	public final String TXT_CATEGORY_LBL = "txtCategoryLabel";
	
	public final String CATEGORY_LBL = "Category Name";
	
	public CategoryForm() throws Exception {
		
	  UIFormStringInput txtCategory = new UIFormStringInput(TXT_CATEGORY_LBL, CATEGORY_LBL, null);
	  txtCategory.setLabel(CATEGORY_LBL);
	  // Add form input.
	  addUIFormInput(txtCategory
	                 .addValidator(MandatoryValidator.class)
	                 .addValidator(SpecialCharacterValidator.class)
	                 .addValidator(StringLengthValidator.class, 5, 50));

	  setActions(new String[]{"Save","Reset","Cancel"});
	  setSubmitAction("Save");
	}
	
	/**
	 * Listens to create/update Category
	 */
	public static class SaveActionListener extends EventListener<CategoryForm> {
	  @Override
	  public void execute(Event<CategoryForm> event) throws Exception {
	    CategoryForm form = event.getSource();
	    Category category = new Category(form.getUIStringInput(form.TXT_CATEGORY_LBL).getValue());
	    BookstoreUtils.getBookstoreService().insertCategory(category);
	    //close the window
	    form.close(form.getParent());
	  }
	}
	
	public static class ResetActionListener extends EventListener<CategoryForm> {

		@Override
		public void execute(Event<CategoryForm> event) throws Exception {
			event.getSource().reset();
		}
	}
	
	public static class CancelActionListener extends EventListener<CategoryForm> {
		@Override
		public void execute(Event<CategoryForm> event) throws Exception {
			CategoryForm form = event.getSource();
			//close the window
			form.close(form.getParent());
		}
	}
	
	  /**
	   * Close the PopupWindow
	   * @param popupWindow
	   */
	  private void close(UIComponent popupWindow) {
		  UIPopupWindow uiPopupWindow = (UIPopupWindow) popupWindow;
		  uiPopupWindow.setShow(false);
	  }

}
