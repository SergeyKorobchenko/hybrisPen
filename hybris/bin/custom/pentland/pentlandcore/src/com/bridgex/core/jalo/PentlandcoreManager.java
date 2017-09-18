package com.bridgex.core.jalo;

import com.bridgex.core.constants.PentlandcoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class PentlandcoreManager extends GeneratedPentlandcoreManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( PentlandcoreManager.class.getName() );
	
	public static final PentlandcoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PentlandcoreManager) em.getExtension(PentlandcoreConstants.EXTENSIONNAME);
	}
	
}
