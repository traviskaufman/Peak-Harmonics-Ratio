Peak Harmonics Ratio Calculator
===============================

This program takes in a data file containing rows populated by both a frequency
and a dB value of a given peak in a spectrum. It treats the first peak in the
list as the fundamental frequency, and from that calculates the sum of all of
the peaks that are harmonics of the fundamental vs. the sum of all the peaks
that are non-harmonic to the fundamental (note that for each frequency it
allows for a %5 error deviation from what the ideal harmonic should be). The
number will be between 0 and 1, and the closer it is to 1, the more likely the
sound that these peaks came from has a definitive pitch to it.

This is a very simple and non-robust program, written for a lab I was doing for
my acoustics class but it gave me a chance to program in Scala which I've been
wanting to do for a long time and perhaps somebody somewhere besides myself
will actually find this useful.

##Peak Data File Format
This program expects a very specific format for the data file. Each line should
be formatted *exactly* as follows:

    {frequency} {amplitude_in_db}

Where `{frequency}` corresponds to the frequency value of a specific peak and
`{amplitude_in_db}` corresponds to the amplitude of that frequency in dB. There
should be one newline character at the end of every line.
The program is very finicky about this format. 

##Building/Running The Code
You will need:

1. [Scala](http://www.scala-lang.org/downloads)

2. [sbt](https://github.com/harrah/xsbt)

Once these are installed and on your $PATH clone this repo, cd into it and then

    $ sbt run

That should do it. To give it a custom file try

    $ sbt "run $FILE_NAME"

##License: WTFPL
This program is free software. It comes without any warranty, to the extend
permitted by applicable law. You can redistribute it and/or modify it under the
terms of the Do What The Fuck You Want To Public License, Version 2, as
published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
details.
