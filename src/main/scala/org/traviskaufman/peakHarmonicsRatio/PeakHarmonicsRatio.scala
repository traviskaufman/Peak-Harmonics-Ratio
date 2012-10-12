/**
 * @file PeakHarmonicsRatio.scala
 *    Reads in optional peak data file as an argument (defaults to
 *    "${Basedir}/data/peaks_amplitudes.txt" and spits out a ratio that
 *    pertains to the sum of all intensities of harmonic peaks over the sum of
 *    all intensities of non-harmonic peaks. The higher the ratio, the more
 *    likely the sound is to have a defined pitch.
 *
 * @author Travis Kaufman
 */
import java.io.FileNotFoundException
import org.traviskaufman.peakHarmonicsRatio.PeakAnalyzer


object Main extends App {
  val peakdata_file = if (args.size > 0) { args(0) } else { "data/peaks_amplitudes.txt" }
  try {
    PeakAnalyzer.parse_peakdata_file(peakdata_file)
  } catch {
    case fnf_e: FileNotFoundException  => {
      System.err.println("ERROR: Could not find file " + peakdata_file)
      System.exit(1)
    }
  }
  val peak_harmonics_ratio: Double = PeakAnalyzer.get_ratio()
  println("Total Harmonic Peaks: " + PeakAnalyzer.harmonic_intensities.size)
  println("Total Non-Harmonics: " + PeakAnalyzer.nonharmonic_intensities.size)
  println("Final Ratio: " + peak_harmonics_ratio)
}
