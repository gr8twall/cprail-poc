package cprail.traincontrol.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties
public class Track implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String _id;
    private String nodeType;
    private String state;
    private String dim;
    private String fill;
    private String stroke;
    private String strokeWidth;
    private String limits;
    private String topType;

    
    public Track() {}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String getNodeType() {
		return nodeType;
	}


	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getDim() {
		return dim;
	}


	public void setDim(String dim) {
		this.dim = dim;
	}


	public String getFill() {
		return fill;
	}


	public void setFill(String fill) {
		this.fill = fill;
	}


	public String getStroke() {
		return stroke;
	}


	public void setStroke(String stroke) {
		this.stroke = stroke;
	}


	public String getStrokeWidth() {
		return strokeWidth;
	}


	public void setStrokeWidth(String strokeWidth) {
		this.strokeWidth = strokeWidth;
	}


	public String getLimits() {
		return limits;
	}


	public void setLimits(String limits) {
		this.limits = limits;
	}


	public String getTopType() {
		return topType;
	}


	public void setTopType(String topType) {
		this.topType = topType;
	}

    
    
    
    
    

	
	
}

//public class Track implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//	
//	private String _id;
//    private String nodeType;
//    private String state;
//    private String event;
////    private String _rev;
////    private String error;
////    private String reason;
//    
//    private Map<String, View> views;
//    
//    public Track() {
//    	this._id = "";
//		this.nodeType = "";
//		this.views = new HashMap<>();
//		this.state = "";
//    }
//    
//	public Track(String trackId, String nodeType) {
//		super();
//		this._id = trackId;
//		this.nodeType = nodeType;
//		this.views = new HashMap<>();
//		this.state = "";
//	}
//
//	public String get_id() {
//		return _id;
//	}
//
//	public void set_id(String _id) {
//		this._id = _id;
//	}
//
//	public String getNodeType() {
//		return nodeType;
//	}
//
//	public void setNodeType(String nodeType) {
//		this.nodeType = nodeType;
//	}
//
//	public Map<String, View> getView() {
//		return views;
//	}
//	
//	public void setView(Map<String, View> views) {
//		this.views = views;
//	}
//
//	public void addView(String key, View view) {
//		this.views.put(key, view);
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getEvent() {
//		return event;
//	}
//
//	public void setEvent(String event) {
//		this.event = event;
//	}
//
//
//
//
//	
//	
//}

