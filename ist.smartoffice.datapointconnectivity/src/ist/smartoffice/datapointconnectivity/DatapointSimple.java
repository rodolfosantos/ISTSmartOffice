package ist.smartoffice.datapointconnectivity;

public class DatapointSimple implements IDatapoint {

	private DatapointAddress address;
	private DatapointMetadata metadata;
	private DatapointValue status;
	

	public DatapointSimple(DatapointAddress address, DatapointMetadata metadata) {
		super();
		this.address = address;
		this.metadata = metadata;
		this.status = new DatapointValue("");
	}

	@Override
	public DatapointAddress getDatapointReadAddress() {
		return address;
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
		return address;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		DatapointSimple other = (DatapointSimple) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		return true;
	}

	@Override
	public DatapointAddress getDatapointMainAddress() {
		String addr = address.getAddress();
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
