/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import java.util.Iterator;

public class TurtleSoup {

	/**
	 * Draw a square.
	 * 
	 * @param turtle     the turtle context
	 * @param sideLength length of each side
	 */
	public static void drawSquare(Turtle turtle, int sideLength) {

		for (int i = 0; i < 4; i++) {
			turtle.forward(sideLength);
			turtle.turn(90);
		}
	}

	/**
	 * Determine inside angles of a regular polygon.
	 * 
	 * There is a simple formula for calculating the inside angles of a polygon; you
	 * should derive it and use it here.
	 * 
	 * @param sides number of sides, where sides must be > 2
	 * @return angle in degrees, where 0 <= angle < 360
	 */
	public static double calculateRegularPolygonAngle(int sides) {
		if (sides < 2) {
			System.exit(-1);
		}
		return 180.0 - 360.0 / (double) sides;

	}

	/**
	 * Determine number of sides given the size of interior angles of a regular
	 * polygon.
	 * 
	 * There is a simple formula for this; you should derive it and use it here.
	 * Make sure you *properly round* the answer before you return it (see
	 * java.lang.Math). HINT: it is easier if you think about the exterior angles.
	 * 
	 * @param angle size of interior angles in degrees, where 0 < angle < 180
	 * @return the integer number of sides
	 */
	public static int calculatePolygonSidesFromAngle(double angle) {
		return (int) (360 / (180 - Math.round(angle)));
	}

	/**
	 * Given the number of sides, draw a regular polygon.
	 * 
	 * (0,0) is the lower-left corner of the polygon; use only right-hand turns to
	 * draw.
	 * 
	 * @param turtle     the turtle context
	 * @param sides      number of sides of the polygon to draw
	 * @param sideLength length of each side
	 */
	public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
//        throw new RuntimeException("implement me!");
		double angle = calculateRegularPolygonAngle(sides);
		for (int i = 0; i < sides; i++) {
			turtle.forward(sideLength);
			turtle.turn(180 - angle);
		}
	}

	/**
	 * Given the current direction, current location, and a target location,
	 * calculate the Bearing towards the target point.
	 * 
	 * The return value is the angle input to turn() that would point the turtle in
	 * the direction of the target point (targetX,targetY), given that the turtle is
	 * already at the point (currentX,currentY) and is facing at angle
	 * currentBearing. The angle must be expressed in degrees, where 0 <= angle <
	 * 360.
	 *
	 * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
	 * 
	 * @param currentBearing current direction as clockwise from north
	 * @param currentX       current location x-coordinate
	 * @param currentY       current location y-coordinate
	 * @param targetX        target point x-coordinate
	 * @param targetY        target point y-coordinate
	 * @return adjustment to Bearing (right turn amount) to get to target point,
	 *         must be 0 <= angle < 360
	 */
	public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY, int targetX,
			int targetY) {
		// throw new RuntimeException("implement me!");
		double absX, absY, degrees;
		absX = Math.abs(currentX - targetX);
		absY = Math.abs(currentY - targetY);
		degrees = absY == 0 ? 90 : Math.toDegrees(Math.atan(absX / absY));
		if (targetX - currentX >= 0)
			degrees = targetY - currentY < 0 ? 180 - degrees : degrees;
		else
			degrees = targetY - currentY >= 0 ? 360 - degrees : 180 + degrees;
		return (degrees + 360 - currentBearing) % 360;
	}

	/**
	 * Given a sequence of points, calculate the Bearing adjustments needed to get
	 * from each point to the next.
	 * 
	 * Assumes that the turtle starts at the first point given, facing up (i.e. 0
	 * degrees). For each subsequent point, assumes that the turtle is still facing
	 * in the direction it was facing when it moved to the previous point. You
	 * should use calculateBearingToPoint() to implement this function.
	 * 
	 * @param xCoords list of x-coordinates (must be same length as yCoords)
	 * @param yCoords list of y-coordinates (must be same length as xCoords)
	 * @return list of Bearing adjustments between points, of size 0 if (# of
	 *         points) == 0, otherwise of size (# of points) - 1
	 */
	public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
		ArrayList<Double> result = new ArrayList<Double>();
		ArrayList<Double> calculateBearings = new ArrayList<Double>();
		result.add(0.0);
		for (int i = 0; i < xCoords.size() - 1; i++) {
			double topoint = calculateBearingToPoint(result.get(i), xCoords.get(i), yCoords.get(i), xCoords.get(i + 1),
					yCoords.get(i + 1));
			calculateBearings.add(topoint);
			result.add(topoint);
		}
		return calculateBearings;
	}

	/**
	 * 
	 * @param p 点1
	 * @param q 点2
	 * @param r 点3
	 * @return 
	 */
	public static boolean orientation(Point p, Point q, Point r) {
		double val = ((q.y() - p.y()) * (r.x() - q.x()) - (q.x() - p.x()) * (r.y() - q.y()));

		if (val == 0)
			return false; // 共线
		return (val > 0) ? false : true; // 顺时针/逆时针
	}

	/**
	 * Given a set of points, compute the convex hull, the smallest convex set that
	 * contains all the points in a set of input points. The gift-wrapping algorithm
	 * is one simple approach to this problem, and there are other algorithms too.
	 * 
	 * @param points a set of points with xCoords and yCoords. It might be empty,
	 *               contain only 1 point, two points or more.
	 * @return minimal subset of the input points that form the vertices of the
	 *         parameter of the convex hull
	 */
	public static Set<Point> convexHull(Set<Point> points) {
		if (points.size() < 3) {
			System.out.println(points);
			return points;
		}
		else {

			// find leftmost point
			List<Point> S = new ArrayList<Point>(points);
			List<Point> P = new ArrayList<Point>(10);
			Point endPoint = null;
			double leftmost = 100.0;
			Point pointOnHull = null;
			// System.out.println(points);
			Iterator<Point> it1 = points.iterator();
			while (it1.hasNext()) {
				Point minPoint = it1.next();
				if (minPoint.x() < leftmost) {
					leftmost = minPoint.x();
					pointOnHull = minPoint;
				}
			}
			int i = 0;
			do {
				// System.out.println(i);
				P.add(i, pointOnHull);
				endPoint = S.get(0);
				for (int j = 1; j < S.size(); j++) {
					if ((endPoint.x() == (pointOnHull.x()) && ((endPoint.y() == pointOnHull.y())))
							|| (orientation(P.get(i), endPoint, S.get(j)))) {
						endPoint = S.get(j);
					}
				}
				i = i + 1;
				pointOnHull = endPoint;

			} while (!(endPoint.x() == P.get(0).x()) || !(endPoint.y() == P.get(0).y()));

			Set<Point> subSet = new HashSet<Point>(P);
			System.out.println(subSet);
			return subSet;
			
		}
	}

	/**
	 * Draw your personal, custom art.
	 * 
	 * Many interesting images can be drawn using the simple implementation of a
	 * turtle. For this function, draw something interesting; the complexity can be
	 * as little or as much as you want.
	 * 
	 * @param turtle the turtle context
	 */
	public static void drawPersonalArt(Turtle turtle) {
		turtle.color(PenColor.GRAY);
		turtle.turn(-45);
		turtle.forward(100);
		int fw = 100, k = 1;
		for (int i = 0; i < 505; i++) {
			turtle.forward(fw);
			turtle.turn(145);
			if (i > 72 * k - 1) {
				fw += 50;
				k += 1;
			}
		}
	}

	/**
	 * Main method.
	 * 
	 * This is the method that runs when you run "java TurtleSoup".
	 * 
	 * @param args unused
	 */
	public static void main(String args[]) {
//		DrawableTurtle turtle = new DrawableTurtle();
//
//		// drawSquare(turtle, 40);
//
//		// draw the window
//		drawPersonalArt(turtle);
//		turtle.draw();

		Point p11 = new Point(1, 1);
		Point p1010 = new Point(10, 10);
		Point p110 = new Point(1, 10);
		Point p12 = new Point(1, 2);
		Point p23 = new Point(2, 3);
		Point p32 = new Point(3, 2);

		Set<Point> points = new HashSet<Point>();
		points.add(p11);
		TurtleSoup.convexHull(points);
		points.add(p1010);
		TurtleSoup.convexHull(points);
		points.add(p110);
		TurtleSoup.convexHull(points);
		points.add(p12);
		TurtleSoup.convexHull(points);

	}

}
