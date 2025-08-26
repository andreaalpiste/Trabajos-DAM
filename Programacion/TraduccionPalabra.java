package terceraev;

public class TraduccionPalabra {
	
	private String palAleman;
	private String traduccion;
	
	

	public TraduccionPalabra(String palAleman) {
		this.palAleman = palAleman;
	}

	public TraduccionPalabra(String palAleman, String traduccion) {
		this.palAleman = palAleman;
		this.traduccion = traduccion;
	}

	public String getPalAleman() {
		return palAleman;
	}

	public void setPalAleman(String palAleman) {
		this.palAleman = palAleman;
	}

	public String getTraduccion() {
		return traduccion;
	}

	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}

	public String toString() {
		return palAleman + " - " + traduccion;
	}
	
	

}
