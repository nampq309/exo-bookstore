package org.exoplatform.book.base;

import javax.jcr.Node;
import javax.jcr.Session;

import org.exoplatform.commons.testing.BaseExoTestCase;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;

@ConfiguredBy({
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.portal-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.identity-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/component.core.test.configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/component.service.test.configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/test.jcr-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/test.portal-configuration.xml")
})

public abstract class BaseTestCase extends BaseExoTestCase {

  @Override
  public void setUp() throws Exception {
    //
    begin();
  }

  @Override
  public void tearDown() throws Exception {
    //
    end();
  }


  @SuppressWarnings("unchecked")
  public <T> T getService(Class<T> clazz) {
    return (T) getContainer().getComponentInstanceOfType(clazz);
  }

}

