package eu.smartcampus.api.historydatastorage.osgi.registries;

import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;
import eu.smartcampus.api.osgi.registries.AbstractServiceRegistry;

/**
 * The Class HistoryDataStorageServiceRegistry.
 */
public final class HistoryDataStorageServiceRegistry extends
		AbstractServiceRegistry<IHistoryDataStorageService> {

	/** The Constant singleton. */
	private final static HistoryDataStorageServiceRegistry singleton = new HistoryDataStorageServiceRegistry();

	/**
	 * Instantiates a new history data storage service registry.
	 */
	private HistoryDataStorageServiceRegistry() {
		// Avoid instantiation
	}

	/**
	 * Gets the single instance of HistoryDataStorageServiceRegistry.
	 * 
	 * @return single instance of HistoryDataStorageServiceRegistry
	 */
	public static HistoryDataStorageServiceRegistry getInstance() {
		return singleton;
	}

}
