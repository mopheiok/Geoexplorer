import java.math.BigDecimal

object MercatorProjection {
  def main(args: Array[String]): Unit = {
    //    setAB(_A, _A)
    //    val startTime = System.currentTimeMillis()
    //    val points = getCircularAreaMapPoints(120.420729, 36.120869, 1)
    //    val endTime = System.currentTimeMillis()
    //    points.foreach(p => println(p.lng + "," + p.lat))
    //    println("程序运行时间：" + (endTime - startTime) + "ms") //输出程序运行时间
    val LB0 = Point(120.420729, 36.120869)
    val LB1 = Point(120.420729, 36.111856)

    val xy: Point = lonLat2Mercator(LB0, LB1)
    println(xy)

    val lngLat: Point = mercator2lonLat(LB0, xy)
    println(lngLat)
  }

  val iterativeTimes = 10
  val iterativeValue = 0.0

  /**
    * 椭球体长轴,千米
    */
  var _A = 6378.137

  var _B = 6356.752314

  var _B0 = 0.0

  var _L0 = 0.0

  val _defaultNumPoints = 40

  def setAB(a: Double, b: Double) {
    if (a <= 0 || b <= 0) return
    _A = a
    _B = b
  }

  def setLB0(pmtL0: Double, pmtB0: Double) {
    val rLng = Math.toRadians(pmtL0)
    if (rLng < -Math.PI || rLng > Math.PI) return
    _L0 = rLng

    val rLat = Math.toRadians(pmtB0)
    if (rLat < -Math.PI / 2 || rLat > Math.PI / 2) return
    _B0 = rLat
  }

  /**
    * 经纬度转XY坐标
    * pmtLB0: 参考点经纬度
    * pmtLB1: 要转换的经纬度
    * 返回值: 直角坐标，单位：公里
    */
  def lonLat2Mercator(pmtLB0: Point, pmtLB1: Point): Point = {
    setLB0(pmtLB0.lng, pmtLB0.lat)

    val B = Math.toRadians(pmtLB1.lat)
    val L = Math.toRadians(pmtLB1.lng)

    var dtemp = 1 - Math.pow(_B / _A, 2)
    if (dtemp < 0) return Point(0, 0)
    val e = Math.sqrt(dtemp) //第一偏心率

    dtemp = Math.pow(_A / _B, 2) - 1
    if (dtemp < 0) return Point(0, 0)
    val e_ = Math.sqrt(dtemp)

    val NB0 = (Math.pow(_A, 2) / _B) / Math.sqrt(1 + Math.pow(e_, 2) * Math.pow(Math.cos(_B0), 2)) //卯酉圈曲率半径

    val K = NB0 * Math.cos(_B0)

    val X = K * (L - _L0)
    val Y = K * Math.log(Math.tan(Math.PI / 4 + B / 2) * Math.pow((1 - e * Math.sin(B)) / (1 + e * Math.sin(B)), e / 2))
    val Y0 = K * Math.log(Math.tan(Math.PI / 4 + _B0 / 2) * Math.pow((1 - e * Math.sin(_B0)) / (1 + e * Math.sin(_B0)), e / 2))

    Point(X, Y - Y0)
  }

  /**
    * XY坐标转经纬度
    * pmtLB0: 参考点经纬度
    * pmtXY: 要转换的XY坐标，单位：公里
    * 返回值: 经纬度
    */
  def mercator2lonLat(pmtLB0: Point, pmtXY: Point): Point = {
    setLB0(pmtLB0.lng, pmtLB0.lat)

    val X = pmtXY.lng
    var Y = pmtXY.lat

    var dtemp = 1 - Math.pow(_B / _A, 2)
    if (dtemp < 0) return Point(0, 0)
    val e = Math.sqrt(dtemp) //第一偏心率

    dtemp = Math.pow(_A / _B, 2) - 1
    if (dtemp < 0) return Point(0, 0)
    val e_ = Math.sqrt(dtemp)

    val NB0 = (Math.pow(_A, 2) / _B) / Math.sqrt(1 + Math.pow(e_, 2) * Math.pow(Math.cos(_B0), 2)) //卯酉圈曲率半径

    val K = NB0 * Math.cos(_B0)

    val Y0 = K * Math.log(Math.tan(Math.PI / 4 + _B0 / 2) * Math.pow((1 - e * Math.sin(_B0)) / (1 + e * Math.sin(_B0)), e / 2))

    Y = Y + Y0

    val L = X / K + _L0

    var B = iterativeValue
    (0 until iterativeTimes).foreach { i =>
      B = Math.PI / 2 - 2 * Math.atan(Math.pow(Math.E, -Y / K) * Math.pow(Math.E, (e / 2) * Math.log((1 - e * Math.sin(B)) / (1 + e * Math.sin(B)))))
    }

    Point(roundDown6(Math.toDegrees(L)), roundDown6(Math.toDegrees(B)))
  }

  /**
    * 保留小数点后6位，不进位
    */
  def roundDown6(value: Double): Double = new BigDecimal(value).setScale(6, BigDecimal.ROUND_DOWN).doubleValue

  def getCircularAreaMapPoints(centerPoint: Point, radius: Double, numPoints: Int): Array[Point] = {
    val phase = 2 * Math.PI / numPoints

    var i = 0
    var dx, dy = 0.0
    val points = new Array[Point](numPoints)
    while (i < numPoints) {
      dx = radius * math.cos(i * phase)
      dy = radius * math.sin(i * phase)

      points.update(i, mercator2lonLat(centerPoint, Point(dx, dy)))
      i += 1
    }
    //    (0 until numPoints).map { i =>
    //      val dx = radius * math.cos(i * phase)
    //      val dy = radius * math.sin(i * phase)
    //
    //      mercator2lonLat(centerPoint, Point(dx, dy))
    //    }.toArray
    points
  }

  def getCircularAreaMapPoints(centerPointLng: Double, centerPointLat: Double, radius: Double): Array[Point] = {
    this.getCircularAreaMapPoints(Point(centerPointLng, centerPointLat), radius, _defaultNumPoints)
  }

  case class Point(lng: Double, lat: Double)

}
