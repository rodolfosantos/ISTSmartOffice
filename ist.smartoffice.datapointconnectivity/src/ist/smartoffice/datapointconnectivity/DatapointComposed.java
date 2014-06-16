package ist.smartoffice.datapointconnectivity;

public class DatapointComposed implements IDatapoint {

	private DatapointAddress readAddress;
	private DatapointAddress writeAddress;
	private DatapointMetadata metadata;
	private DatapointValue status;

	public DatapointComposed(DatapointAddress readAddress,
			DatapointAddress writeAddress, DatapointMetadata metadata) {
		super();
		this.readAddress = readAddress;
		this.writeAddress = writeAddress;
		this.metadata = metadata;
		this.status = new DatapointValue("");
	}

	@Override
	public DatapointAddress getDatapointReadAddress() {
		return readAddress;
	}

	@Override
	public DatapointMetadata getDatapointMetadata() {
		return metadata;
	}

	@Override
	public void setDatapointMetadata(DatapointMetadata metadata) {
		this.metadata = metadata;

	}

	@Override
	public DatapointAddress getDatapointWriteAddress() {
		return writeAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((readAddress == null) ? 0 : readAddress.hashCode());
		result = prime * result
				+ ((writeAddress == null) ? 0 : writeAddress.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatapointComposed other = (DatapointComposed) obj;
		if (readAddress == null) {
			if (other.readAddress != null)
				return false;
		} else if (!readAddress.equals(other.readAddress))
			return false;
		if (writeAddress == null) {
			if (other.writeAddress != null)
				return false;
		} else if (!writeAddress.equals(other.writeAddress))
			return false;
		return true;
	}

	@Override
	public DatapointAddress getDatapointMainAddress() {
		String addr = readAddress.getAddress() + ":" + writeAddress.getAddress();
		return new DatapointAddress(addr);
	}

	@Override
	public DatapointValue getStatus() {
		return status;
	}

	@Override
	public void setStatus(DatapointValue value) {
		this.status = value;

	}

	@Override
	public void setStatus(String value) {
		this.status = new DatapointValue(value);

	}

}
