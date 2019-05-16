package com.blobExample;

import com.blobExample.client.ClientApplication;
import com.blobExample.server.ServerApplication;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class SampleApplication {
    public static void main(String[] args) throws Exception {

        if(args == null)
            throw new Exception("Arguments not provided");

        if(String.format("sampleClient").equalsIgnoreCase(args[0])){
            new ClientApplication().start(args);
        }
        else if(String.format("sampleServer").equalsIgnoreCase(args[0])){
            new ServerApplication().start(args);
        }
        else if(String.format("benchmark").equalsIgnoreCase(args[0])){
            initBenchmarking();
        }
        else{
            throw new Exception(String.format("unexpected argument").concat(" ").concat(args[0]));
        }

    }

    private static void initBenchmarking() {
        ResultFormatType resultsFileOutputType = ResultFormatType.JSON;
        String resultFilePrefix = "jmh-";

        Options opt = new OptionsBuilder()
                .include(".*Benchmark")
                .warmupIterations(5)
                .measurementIterations(5)
                .timeUnit(TimeUnit.MILLISECONDS)
                .forks(1)
                .threads(1)
                .resultFormat(resultsFileOutputType)
                .result(buildResultsFileName(resultFilePrefix, resultsFileOutputType))
                .jvmArgs("-server", "-Xms2048m", "-Xmx2048m")
                .build();
        try {
            new Runner(opt).run();
        } catch (RunnerException e) {
            e.printStackTrace();
        }
    }

    private static String buildResultsFileName(String resultFilePrefix, ResultFormatType resultType) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

        String suffix;
        switch (resultType) {
            case CSV:
                suffix = ".csv";
                break;
            case SCSV:
                // Semi-colon separated values
                suffix = ".scsv";
                break;
            case LATEX:
                suffix = ".tex";
                break;
            case JSON:
            default:
                suffix = ".json";
                break;

        }

        return String.format("%s%s%s", resultFilePrefix, date.format(formatter), suffix);
    }

}
