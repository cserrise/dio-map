package de.unima.ki.dio.exceptions;

public class RockitException extends DioException {
	
	public static final int IO_ERROR = 1;
	
	public RockitException(int generalDescriptionId, String specificDescription, Exception e) {
		this(generalDescriptionId, specificDescription);
		this.catchedException = e;
	}	
	
	public RockitException(int generalDescriptionId, String specificDescription) {
		this(generalDescriptionId);
		this.specificDescription = specificDescription;
	}
	
	public RockitException(int generalDescriptionId) {
		super("Rockit-Exception");
		switch (generalDescriptionId) {
		case IO_ERROR:
			this.generalDescription = "IO-operation caused an error";
			break;
		default:
			this.generalDescription = "general description is missing";
			break;
		}
	}
	
	private static final long serialVersionUID = 1L;

}
