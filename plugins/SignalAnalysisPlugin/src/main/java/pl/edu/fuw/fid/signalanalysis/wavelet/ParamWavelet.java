package pl.edu.fuw.fid.signalanalysis.wavelet;

/**
 * Base class for wavelet with additional parameter.
 *
 * @author ptr@mimuw.edu.pl
 */
public abstract class ParamWavelet extends MotherWavelet {

	protected final double param;

	public ParamWavelet(Double param) {
		this.param = param;
	}

}
