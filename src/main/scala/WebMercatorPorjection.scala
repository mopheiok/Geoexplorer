

object WebMercatorPorjection {
  def main(args: Array[String]): Unit = {
    //    val points = getCirclePoints(120.420729, 36.120869, 1)
    //    points.foreach(println)
    val xy = lonLat2Mercator_(120.420729, 36.12988)
    println(xy)

    val lngLat = mercator2lonLat(xy)
    println(lngLat)
  }

  case class Point(x: Double, y: Double)

  /**
    * 地球半径,千米
    */
  val _R = 6378.137

  def lonLat2Mercator_(lng: Double, lat: Double): Point = {
    val x = _R * Math.toRadians(lng)
    val y = _R * Math.log(Math.tan(Math.PI / 4 + Math.toRadians(lat) / 2))
    Point(x, y)
  }

  def mercator2lonLat(mercator: Point): Point = {
    val rlng = mercator.x / _R
    val rlat = 2 * Math.atan(Math.exp(mercator.y / _R)) - Math.PI / 2

    Point(Math.toDegrees(rlng), Math.toDegrees(rlat))
  }

  def getCirclePoints(centerLng: Double, centerLat: Double, radius: Double): Array[Point] = {
    val phase = 2 * Math.PI / 40
    val centerPoint = lonLat2Mercator_(centerLng, centerLat)
    (0 until 40).map { i =>
      val dx = centerPoint.x + radius * Math.cos(i * phase)
      val dy = centerPoint.y + radius * Math.sin(i * phase)

      mercator2lonLat(Point(dx, dy))
    }.toArray
  }
}
