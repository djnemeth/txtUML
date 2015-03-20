package hu.elte.txtuml.layout.visualizer.model;

import hu.elte.txtuml.layout.visualizer.helpers.Helper;

import java.util.ArrayList;

/***
 * Class representing a link in a diagram. The abstract model consists of the
 * name of the link, the name of the starting object and the name of the ending
 * object. The physical line is represented with a series of Points, which are
 * accesible via the getRoute() method.
 * 
 * @author Bal�zs Gregorics
 *
 */
public class LineAssociation
{
	private String _id;
	private String _from;
	private String _to;
	private AssociationType _type;
	private ArrayList<Point> _route;
	private Integer _turns;
	private Integer _extends;
	
	// Getters, setters
	
	/***
	 * Getter for the name/id of the link.
	 * 
	 * @return String of the link's name/id.
	 */
	public String getId()
	{
		return _id;
	}
	
	/***
	 * Setter for modifying the link's name/id.
	 * 
	 * @param value
	 *            String name to modify.
	 */
	public void setId(String value)
	{
		_id = value;
	}
	
	/***
	 * Getter for the name of the link's startinging object.
	 * 
	 * @return String of the object's name.
	 */
	public String getFrom()
	{
		return _from;
	}
	
	/***
	 * Setter for modifying the link's Start object's name.
	 * 
	 * @param value
	 *            The name of the object to modify the start.
	 */
	public void setFrom(String value)
	{
		_from = value;
	}
	
	/***
	 * Getter for the name of the link's ending object.
	 * 
	 * @return String of the object's name.
	 */
	public String getTo()
	{
		return _to;
	}
	
	/***
	 * Setter for modifying the link's End object's name.
	 * 
	 * @param value
	 *            The name of the object to modify the end.
	 */
	public void setTo(String value)
	{
		_to = value;
	}
	
	/***
	 * Getter for the link's type.
	 * 
	 * @return AssociationType of the link.
	 */
	public AssociationType getType()
	{
		return _type;
	}
	
	/***
	 * Setter for modifying the type of the link.
	 * 
	 * @param value
	 *            Type to modify the link.
	 */
	public void setType(AssociationType value)
	{
		_type = value;
	}
	
	/**
	 * Getter for a simplyfied route. (Removed straight points)
	 * 
	 * @return ArrayList of points.
	 */
	public ArrayList<Point> getMinimalRoute()
	{
		ArrayList<Point> result = Helper.clonePointList(_route);
		
		if (result.size() < 2)
			return result;
		
		for (int i = 1; i < result.size() - 1; i++)
		{
			while (i < result.size() - 1
					&& (result.get(i - 1).getX() == result.get(i + 1).getX() || result
							.get(i - 1).getY() == result.get(i + 1).getY()))
			{
				result.remove(i);
			}
		}
		
		return result;
	}
	
	/***
	 * Getter for the Link's Route of Points.
	 * 
	 * @return ArrayList of Points.
	 */
	public ArrayList<Point> getRoute()
	{
		return _route;
	}
	
	/***
	 * Enumaration representing the choice to select a route's starting or
	 * ending point.
	 * 
	 * @author Bal�zs Gregorics
	 *
	 */
	public enum RouteConfig
	{
		/**
		 * Starting point of the route.
		 */
		START,
		/**
		 * Ending point of the route.
		 */
		END
	}
	
	/***
	 * Getter for the end points of the link's route.
	 * 
	 * @param conf
	 *            RouteConfig to determine the end of the link.
	 * @return Point representing the desired end point of the link's route.
	 */
	public Point getRoute(RouteConfig conf)
	{
		if (conf.equals(RouteConfig.START))
		{
			try
			{
				return _route.get(0);
			}
			catch (IndexOutOfBoundsException e)
			{
				return null;
			}
		}
		if (conf.equals(RouteConfig.END))
		{
			try
			{
				return _route.get(_route.size() - 1);
			}
			catch (IndexOutOfBoundsException e)
			{
				return null;
			}
		}
		
		return null;
	}
	
	/***
	 * Setter for modifying the route of the link.
	 * 
	 * @param value
	 *            ArrayList of Points of the route.
	 */
	public void setRoute(ArrayList<Point> value)
	{
		_route = value;
	}
	
	/***
	 * Getter for the LineAssociation's anchor points.
	 * 
	 * @param conf
	 *            RouteConfig to determine which end of the link you want.
	 * @param objectWidth
	 *            The width of the object (in grid number) which this links
	 *            connects to on the side you request.
	 * @return Anchor of double[2] or null if no such RouteConfig exists.
	 */
	public double[] getAnchor(RouteConfig conf, Integer objectWidth)
	{
		double[] result = new double[2];
		
		Point first;
		Point second;
		Integer toBounds = (objectWidth == 1) ? 1 : (objectWidth - 1) / 2;
		
		switch (conf)
		{
			case START:
				first = Point
						.Substract(_route.get(0), new Point(-1 * toBounds, toBounds));
				second = _route.get(1);
				break;
			case END:
				first = Point.Substract(_route.get(_route.size() - 1), new Point(-1
						* toBounds, toBounds));
				second = _route.get(_route.size() - 2);
				break;
			default:
				return null;
		}
		
		Point vec = Point.Substract(first, second);
		double length = Math.sqrt(vec.getX() * vec.getX() + vec.getY() * vec.getY());
		result[0] = (vec.getX() / length + 1) / 2;
		result[1] = (vec.getY() / length + 1) / 2;
		
		return result;
	}
	
	/***
	 * Getter for the turns the link makes during it's route.
	 * 
	 * @return Number of turns the route makes during it's route.
	 */
	public Integer getTurns()
	{
		return _turns;
	}
	
	/***
	 * Setter for the turns the link makes during it's route.
	 * 
	 * @param value
	 *            Number of turns.
	 */
	public void setTurns(Integer value)
	{
		_turns = value;
	}
	
	/***
	 * Getter for the extensions the graph search algorithm made to find the
	 * route for this link.
	 * 
	 * @return Number of extentions.
	 */
	public Integer getExtends()
	{
		return _extends;
	}
	
	/***
	 * Setter for the extensions the graph search algorithm made to find the
	 * route for this link.
	 * 
	 * @param value
	 *            Number of extentiont.
	 */
	public void setExtends(Integer value)
	{
		_extends = value;
	}
	
	// End Getters, setters
	
	// Ctors
	
	/***
	 * Create a representation of a link.
	 * 
	 * @param n
	 *            Name of the link.
	 * @param f
	 *            Name of the object From the link origins.
	 * @param t
	 *            Name of the object To the link connects.
	 */
	public LineAssociation(String n, String f, String t)
	{
		_id = n;
		_from = f;
		_to = t;
		_route = new ArrayList<Point>();
		_turns = 0;
		_extends = 0;
		_type = AssociationType.normal;
	}
	
	/***
	 * Create a representation of a link.
	 * 
	 * @param n
	 *            Name of the link.
	 * @param f
	 *            Name of the object From the link origins.
	 * @param t
	 *            Name of the object To the link connects.
	 * @param ty
	 *            Type of the link.
	 */
	public LineAssociation(String n, String f, String t, AssociationType ty)
	{
		this(n, f, t);
		_type = ty;
	}
	
	/***
	 * Create a representation of a link.
	 * 
	 * @param n
	 *            Name of the link.
	 * @param f
	 *            The object From the link origins.
	 * @param t
	 *            The object To the link connects.
	 */
	public LineAssociation(String n, RectangleObject f, RectangleObject t)
	{
		_id = n;
		_from = f.getName();
		_to = t.getName();
		_route = new ArrayList<Point>();
		_route.add(f.getPosition());
		_route.add(t.getPosition());
		_turns = 0;
		_extends = 0;
		_type = AssociationType.normal;
	}
	
	/***
	 * Create a representation of a link.
	 * 
	 * @param n
	 *            Name of the link.
	 * @param f
	 *            The object From the link origins.
	 * @param t
	 *            The object To the link connects.
	 * @param ty
	 *            Type of the link.
	 */
	public LineAssociation(String n, RectangleObject f, RectangleObject t,
			AssociationType ty)
	{
		this(n, f, t);
		_type = ty;
	}
	
	/***
	 * Create a representation of a link.
	 * 
	 * @param n
	 *            Name of the link.
	 * @param f
	 *            Name of the object From the link origins.
	 * @param t
	 *            Name of the object To the link connects.
	 * @param s
	 *            Point of the object From the link starts.
	 * @param e
	 *            Point of the object To the link connects.
	 */
	public LineAssociation(String n, String f, String t, Point s, Point e)
	{
		_id = n;
		_from = f;
		_to = t;
		_route = new ArrayList<Point>();
		_route.add(s);
		_route.add(e);
		_turns = 0;
		_extends = 0;
		_type = AssociationType.normal;
	}
	
	/***
	 * Create a representation of a link.
	 * 
	 * @param n
	 *            Name of the link.
	 * @param f
	 *            Name of the object From the link origins.
	 * @param t
	 *            Name of the object To the link connects.
	 * @param s
	 *            Point of the object From the link starts.
	 * @param e
	 *            Point of the object To the link connects.
	 * @param ty
	 *            Type of the link.
	 */
	public LineAssociation(String n, String f, String t, Point s, Point e,
			AssociationType ty)
	{
		this(n, f, t, s, e);
		_type = ty;
	}
	
	/**
	 * Copy LineAssociation.
	 * 
	 * @param a
	 *            An already existing LineAssociation to copy.
	 */
	public LineAssociation(LineAssociation a)
	{
		this(a.getId(), a.getFrom(), a.getTo(), a.getType());
		_route = Helper.clonePointList(a.getRoute());
		_extends = new Integer(a.getExtends());
		_turns = new Integer(a.getTurns());
	}
	
	// End Ctors
	
	// Statics
	
	/***
	 * Equality comparison for two LineAssociation objects.
	 * 
	 * @param a1
	 *            First LineAssociation to compare.
	 * @param a2
	 *            Second LineAssociation to compare.
	 * @return Boolean value of a1 equals a2.
	 */
	public static boolean Equals(LineAssociation a1, LineAssociation a2)
	{
		return a1.equals(a2);
	}
	
	// End Statics
	
	// Equality
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
		if (this instanceof LineAssociation && obj instanceof LineAssociation)
		{
			LineAssociation a1 = (LineAssociation) this;
			LineAssociation a2 = (LineAssociation) obj;
			return a1._id.equals(a2._id);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _id.hashCode();
		result = prime * result + _from.hashCode();
		result = prime * result + _to.hashCode();
		result = prime * result + _type.hashCode();
		return result;
	}
	
	// End Equality
	
	// Methods
	
	@Override
	public LineAssociation clone()
	{
		LineAssociation result = new LineAssociation(this);
		
		return result;
	}
	
	@Override
	public String toString()
	{
		String result = _id + ": ";
		result += "(" + _from + " - " + _to + ")";
		result += " (" + _type.toString() + ")";
		if (_route != null && _route.size() > 0)
		{
			result += ", #Turns: " + _turns + ", #Extends: " + _extends;
			result += ", Route(" + (_route.size() - 1) + "): ";
			for (Point p : _route)
				result += p.toString() + "->";
			// result.substring(0, result.length() - 2);
		}
		
		return result;
	}
	
	// end Methods
	
}
