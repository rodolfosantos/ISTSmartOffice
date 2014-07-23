/**
 * 
 */
package ist.smartoffice.datapointconnectivity;

/**
 * 
 * 
 */
public class DatapointAddress implements Comparable<DatapointAddress> {

	private String address;

	public DatapointAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatapointAddress other = (DatapointAddress) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return address;
	}

	@Override
	public int compareTo(DatapointAddress other) {
		String thisS = this.address;
		String otherS =  other.getAddress();
		return thisS.compareTo(otherS);
	}

}
