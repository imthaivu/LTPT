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

public class SinhMaTheoNgay implements IdentifierGenerator, Configurable {

	/**
	 * Mã viết tắt của bảng VD: Nhân viên là NV
	 */
	private String maVietTat;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		LocalDate date = LocalDate.now();
		String dateString = "" + date.getYear() % 100
				+ (date.getMonthValue() >= 10 ? date.getMonthValue() : "0" + date.getMonthValue());
		// Lấy mã trong bảng VD:bảng NhanVien thì lấy maNhanVien
		String maSQLSelect = session.getEntityPersister(object.getClass().getName(), object)
				.getIdentifierPropertyName();
		// Lấy tên bảng trong SQL
		String tenBangSQL = object.getClass().getSimpleName();

		String query = String.format("select %s from %s where %s like '%s'", maSQLSelect, tenBangSQL, maSQLSelect,
				maVietTat + dateString + "%");

		Stream<String> ids = session.createQuery(query).stream();

		if (ids == null) {
			return maVietTat + dateString + (String.format("%04d", 1));
		} else {
			Long max = ids.map(o -> o.replace(o.substring(0, 6), "")).mapToLong(Long::parseLong).max().orElse(0L);
			return maVietTat + dateString + (String.format("%04d", max + 1));
		}

	}

	@Override
	public void configure(org.hibernate.type.Type type, java.util.Properties params, ServiceRegistry serviceRegistry)
			throws org.hibernate.MappingException {
		maVietTat = params.getProperty("prefix");
	}

}
