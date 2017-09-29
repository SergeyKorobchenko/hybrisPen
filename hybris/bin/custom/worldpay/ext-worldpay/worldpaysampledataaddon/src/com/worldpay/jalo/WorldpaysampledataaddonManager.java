package com.worldpay.jalo;

import com.worldpay.constants.WorldpaysampledataaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class WorldpaysampledataaddonManager extends GeneratedWorldpaysampledataaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( WorldpaysampledataaddonManager.class.getName() );
	
	public static final WorldpaysampledataaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (WorldpaysampledataaddonManager) em.getExtension(WorldpaysampledataaddonConstants.EXTENSIONNAME);
	}
	
}
