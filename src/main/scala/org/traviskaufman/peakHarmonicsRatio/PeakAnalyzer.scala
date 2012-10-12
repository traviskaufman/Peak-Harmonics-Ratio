/**
 * @file PeakAnalyzer.scala
 *    This program reads a text file which should contain a series of rows,
 *    each containing two columns which are separated by a space. The first
 *    column should contain the frequency of a peak, and the second column
 *    containing the amplitude at that particular peak. It treats the first
 *    peak as the fundamental frequency, and calculates the ratio of the sum of
 *    all of the intensities of the harmonic peaks over the sum of all of the
 *    intensities of the non-harmonic peaks. A standard deviation of 5% is
 *    given to determine the harmonicity of the peaks.
 *
 * Usage: PeakAnalyzer [file]
 *
 * @author Travis Kaufman
 */
package org.traviskaufman.peakHarmonicsRatio

import scala.math._
import scala.collection.mutable.ListMap
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


// I get that there's no separation of concerns here. Oh well....
object PeakAnalyzer {
  /**
   * The standard sound intensity reference.
   * @const
   */
  val SIL_REF: Double = pow(10, -12)

  val harmonic_intensities: ArrayBuffer[Double] = new ArrayBuffer()

  val nonharmonic_intensities: ArrayBuffer[Double] = new ArrayBuffer()

  /**
   * Map to hold the peakdata information. Peakdata is given in key/value pair
   * (frequency, SIL).
   *
   * @private
   */
  private val peak_data: ArrayBuffer[List[Double]] = new ArrayBuffer()

  /** Fundamental Frequency **/
  private var fundamental_freq: Double = 0

  /**
   * Converts a decibel value to its corresponding intensity value using the
   * equation I = I(0) * 10^(db_value/10) where I(0) is the standard sound
   * intensity reference 10^-12 w/m^2
   *
   * @param double db_value
   *    The decibel value you want converted into an intensity value.
   *
   * @return Double
   *    Returns the converted decibel value as an intensity value.
   *
   * @private
   */
  private def db_to_sil(db_value: Double): Double = {
    SIL_REF * pow(10, (db_value / 10))
  }

  /**
   * Parses a text file formatted properly (see program description)
   * @return PeakAnalyzer
   *    Returns itself for chainability.
   */
  def parse_peakdata_file(file_name: String): this.type = {
    val peak_value_fd = Source.fromFile(file_name)
    val peak_value_data = peak_value_fd.mkString
    peak_value_fd.close
    val pv_array: Array[String] = peak_value_data.split("\n")
    fundamental_freq = pv_array(0).split(" ")(0).toDouble
    pv_array.foreach { str =>
      val pv_tuple = str.split(" ").map { item => item.toDouble }
      peak_data += List(pv_tuple(0), db_to_sil(pv_tuple(1)))
    }
    this
  }

  /**
   * Get the ratio of the sum of harmonic peak intensities to non-harmonic peak
   * intensities.
   *
   * @param percent_deviation
   *    The percent of deviation that will be given to peak values to determine
   *    whether or not they are a harmonic.
   *
   * @return Double
   *    Exactly what the description said.
   */
  def get_ratio(percent_deviation: Int = 5): Double = {
    val err_factor = 0.01 * percent_deviation
    var current_harm_index: Int = 1
    def is_harmonic(freq: Double, harm_nbr: Int): Boolean = {
      val ideal_freq = fundamental_freq * harm_nbr
      val deviation_amt = ideal_freq * err_factor
      (freq < ideal_freq + deviation_amt) &&
      (freq > ideal_freq - deviation_amt)
    }
    peak_data.foreach { kv_pair =>
      val freq: Double = kv_pair(0)
      val intensity: Double = kv_pair(1)
      print("Analyzing " + freq + "...")
      if (is_harmonic(freq, current_harm_index)) {
        current_harm_index += 1
        harmonic_intensities += intensity
        println("harmonic")
      } else {
        nonharmonic_intensities += intensity
        println("nonharmonic")
      }
    }
    harmonic_intensities.sum / nonharmonic_intensities.sum
  }
}
