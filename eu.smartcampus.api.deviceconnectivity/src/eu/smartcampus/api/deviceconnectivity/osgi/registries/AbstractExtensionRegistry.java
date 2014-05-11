package eu.smartcampus.api.deviceconnectivity.osgi.registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractExtensionRegistry.
 * 
 * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
 * 
 * @param <T>
 *            the service type
 */
public abstract class AbstractExtensionRegistry<T> implements
		ILuminaExtensionRegistry<T> {

	/** The service factories by name. */
	private Map<String, T> servicesByName = new HashMap<String, T>();

	/** The service listeners. */
	private List<ServiceRegistryListener> serviceListeners = new ArrayList<ServiceRegistryListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#addServiceListener
	 * (lumina .core.osgi.registries.IServiceRegistry.ServiceRegistryListener)
	 */
	@Override
	public final void addServiceListener(ServiceRegistryListener l) {
		if (!serviceListeners.contains(l))
			serviceListeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#removeServiceListener
	 * (lumina .core.osgi.registries.IServiceRegistry.ServiceRegistryListener)
	 */
	@Override
	public final void removeServiceListener(ServiceRegistryListener l) {
		serviceListeners.remove(l);
	}

	/**
	 * Notify service added.
	 * 
	 * @param serviceName
	 *            the service name
	 */
	protected final void notifyServiceAdded(String serviceName) {
		for (ServiceRegistryListener l : serviceListeners)
			l.serviceAdded(serviceName);
	}

	/**
	 * Notify service modified.
	 * 
	 * @param serviceName
	 *            the service name
	 */
	protected final void notifyServiceModified(String serviceName) {
		for (ServiceRegistryListener l : serviceListeners)
			l.serviceModified(serviceName);
	}

	/**
	 * Notify service removed.
	 * 
	 * @param serviceName
	 *            the service name
	 */
	protected final void notifyServiceRemoved(String serviceName) {
		for (ServiceRegistryListener l : serviceListeners)
			l.serviceRemoved(serviceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#modifyService(java
	 * .lang. String, lumina.core.osgi.factories.ILuminaExtensionFactory)
	 */
	@Override
	public final void modifyService(String serviceName, T service) {
		servicesByName.remove(serviceName);
		servicesByName.put(serviceName, service);
		notifyServiceModified(serviceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#removeService(java
	 * .lang. String)
	 */
	@Override
	public final void removeService(String serviceName) {
		servicesByName.remove(serviceName);
		notifyServiceRemoved(serviceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#addService(java.
	 * lang.String, lumina.core.osgi.factories.ILuminaExtensionFactory)
	 */
	@Override
	public final void addService(String serviceName, T service) {
		servicesByName.put(serviceName, service);
		notifyServiceAdded(serviceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#getRegisteredServices
	 * ()
	 */
	@Override
	public final String[] getRegisteredServicesNames() {
		Set<String> driverNames = servicesByName.keySet();
		String[] names = new String[driverNames.size()];
		int i = 0;
		for (String d : driverNames)
			names[i++] = d;
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lumina.core.osgi.registries.ILuminaExtensionRegistry#getService(java.
	 * lang.String)
	 */
	@Override
	public final T getService(String serviceName) {
		// TODO: Adicionar o fuzzy search do LDAP?
		return servicesByName.get(serviceName);
	}
}
