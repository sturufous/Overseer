package ca.bc.gov.tno.overseer;

import java.math.BigDecimal;
import java.util.List;

//Generated Dec 24, 2019, 8:06:31 AM by Hibernate Tools 5.0.6.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.query.Query;

/**
* AutoRun generated by hbm2java
*/
@Entity
@Table(name = "JOREL2_HOSTS", schema = "TNO")
public class Jorel2HostsDao implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String hostIp;
	private String port;
	private String description;

	public Jorel2HostsDao() {
	}

	public Jorel2HostsDao(String hostIp) {
		this.hostIp = hostIp;
	}

	public Jorel2HostsDao(String hostIp, String port) {
		this.hostIp = hostIp;
		this.port = port;
	}

	@Id

	@Column(name = "HOST_IP", unique = true, length = 20)
	public String getHostIp() {
		return this.hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	@Column(name = "PORT", length = 20)
	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "DESCRIPTION", length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get a list of all Jorel2 hosts that can be monitored by Overseer. 
	 * 
	 * @param session The current Hibernate persistence context.
	 * @return A list containing all the records that meet the expiry criteria.
	 */
	public static List<Jorel2HostsDao> getJorel2Hosts(Session session) {
		
		String sqlStmt = "from Jorel2HostsDao";

		Query<Jorel2HostsDao> query = session.createQuery(sqlStmt, Jorel2HostsDao.class);
        List<Jorel2HostsDao> results = query.getResultList();
        
		return results;
	}
}
