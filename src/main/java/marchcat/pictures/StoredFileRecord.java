package marchcat.pictures;

import java.sql.Timestamp;

//Same as in the MCStorage
public record StoredFileRecord(
		String originalName,
		String hashName,
		String extension,
		long size,
		Timestamp storedAt) {

}
