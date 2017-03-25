package MTPlayer;

import java.io.Serializable;

public class FileEvent implements Serializable {
	
	private String destinationDirectory;
	private String sourceDirectory;
	private String filename;
	private long fileSize;
	private byte[] fileData;
	private String status;
	private static final long serialVersionUID = 1L;
	
	public FileEvent() {
		
	}
	
	public String getDestinationDirectory() { return destinationDirectory; }
	public String getSourceDirectory() { return sourceDirectory; }
	public String getFilename() { return filename; }
	public long getFileSize() { return fileSize; }
	public String getStatus() { return status; }
	public byte[] getFileData() { return fileData; }
	
	public void setDestinationDirectory(String destinationDirectory) { this.destinationDirectory = destinationDirectory; }
	public void setSourceDirectory(String sourceDirectory) { this.sourceDirectory = sourceDirectory; }
	public void setFilename(String filename) { this.filename = filename; }
	public void setFileSize(long fileSize) { this.fileSize = fileSize; }
	public void setStatus(String status) { this.status = status; }
	public void setFileData(byte[] fileData) { this.fileData = fileData; }
}
