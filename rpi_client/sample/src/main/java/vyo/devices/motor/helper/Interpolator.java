package vyo.devices.motor.helper;

/**
 * Interpolate value in the range [0,1]
 * See the documented function graphs
 *
 * @author Benny
 */
public class Interpolator {

    public enum functions { NONE, SIGMOID, LOGARITHMIC, EXPONENTIAL }

    private static final int DEFAULT_FUNC = 0;
    private static final int DEFAULT_CURVE = 15;
    public static final int DEFAULT_SAMPLER = 100;
    private static final int NUM_OF_FUNC = functions.values().length;


    public static float[] interpolate(int startPos, int endPos, int numOfSamples) {
        return interpolate(startPos, endPos, DEFAULT_FUNC, numOfSamples);
    }

    public static float[] interpolate(int startPos, int endPos, int function, int numOfSamples) {

        float[] samples = new float[numOfSamples];

        if (function < 0 || function >= NUM_OF_FUNC) {
            function = 0;
        }

        switch (function) {
            case 0:
                    samples = new float[] { endPos };
                break;
            case 1:
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = startPos + (sigmoid(i / numOfSamples) * endPos);
                }
                break;
            case 2:
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = startPos + (logarithmic(i / numOfSamples) * endPos);
                }
                break;
            case 3:
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = startPos + (exponential(i / numOfSamples) * endPos);
                }
                break;
        }

        return samples;
    }

    /**
     * Interpolation functions: x range is [0,1]
     *
     * @return float number in the range [0,1]
     */

    private static float sigmoid(float x) {
        // map x range from [0,1] -> [0,12]
        x *= 12f;

        return (float) (0.5 + 0.5 * Math.tanh(0.5 * (x - 6)));
    }

    private static float logarithmic(float x, int curve) {
        return (float) ((Math.pow(curve, -x) / -(curve - 1)) * curve);
    }

    private static float logarithmic(float x) {
        return logarithmic(x, DEFAULT_CURVE);
    }

    private static float exponential(float x, int curve) {
        return (float) (Math.pow(curve, x) / (curve - 1));
    }

    private static float exponential(float x) {
        return exponential(x, DEFAULT_CURVE);
    }
}
