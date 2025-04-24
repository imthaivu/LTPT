package generator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;

public class SinhMaTuDong implements IdentifierGenerator, Configurable {

	/**
	 * Mã viết tắt của bảng VD: Nhân viên là NV
	 */
	private String maVietTat;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		// Lấy mã trong bảng VD:bảng NhanVien thì lấy maNhanVien
		String maSQLSelect = session.getEntityPersister(object.getClass().getName(), object)
				.getIdentifierPropertyName();
		// Lấy tên bảng trong SQL
		String tenBangSQL = object.getClass().getSimpleName();

		String query = String.format("select %s from %s", maSQLSelect, tenBangSQL);
		
		Stream<String> ids = session.createQuery(query).stream();
			Long max = ids.map(o -> o.replace(maVietTat, "")).mapToLong(Long::parseLong).max().orElse(0L);
			return maVietTat + (String.format("%05d", max + 1));

	}

	@Override
	public void configure(org.hibernate.type.Type type, java.util.Properties params, ServiceRegistry serviceRegistry)
			throws org.hibernate.MappingException {
		maVietTat = params.getProperty("prefix");
	}

}
