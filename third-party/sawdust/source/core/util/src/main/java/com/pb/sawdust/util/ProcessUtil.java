package com.pb.sawdust.util;

import com.pb.sawdust.io.FlushingOutputStream;
import com.pb.sawdust.util.annotations.Beta;
import com.pb.sawdust.util.exceptions.RuntimeIOException;
import com.pb.sawdust.util.exceptions.RuntimeInterruptedException;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code ProcessUtil} provides convenience methods for running processes from Java. It is basically a wrapper around
 * the {@code java.lang.ProcessBuilder} and {@code java.lang.Process} classes, providing simple interactions with the
 * standard out, error, and in streams. Commands to run are specified as a list of strings containing the main program and
 * its arguments.
 *
 * @author crf <br/>
 *         Started 3/14/11 6:16 PM
 */
@Beta
public class ProcessUtil {
    private static int STDOUT_BUFFER_SIZE = 20; //small, to prevent jerky output

    /**
     * Run a process with the given command and wait for it to finish. The standard out stream will be sent to {@code System.out},
     * as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     */
    public static int runProcess(List<String> command) {
        return runProcess(command,null,System.out,null,null,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output stream to use for the standard
     * out. The standard error stream will be sent to the {@code out} stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, OutputStream out) {
        return runProcess(command,null,out,null,null,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output streams to use for the standard
     * out and the standard error.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, OutputStream stdOut, OutputStream stdErr) {
        return runProcess(command,null,stdOut,stdErr,null,false);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the input streams to use for the standard
     * in. The standard out stream will be sent to {@code System.out}, as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, InputStream in) {
        return runProcess(command,null,System.out,null,in,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output streams to use for the standard
     * out and the standard error, and the input stream to use for the standard input.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, OutputStream out, InputStream in) {
        return runProcess(command,null,out,null,in,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output stream to use for the standard
     * out and the input stream to use for the standard input.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, OutputStream stdOut, OutputStream stdErr, InputStream in) {
        return runProcess(command,null,stdOut,stdErr,in,false);
    }          
    /**
     * Run a process with the given command and wait for it to finish, specifying the working directory to run the process
     * in. The standard out stream will be sent to {@code System.out}, as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, File workingDirectory) {
        return runProcess(command,workingDirectory,System.out,null,null,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output stream to use for the standard
     * out, as well as the working directory to run the process in. The standard error stream will be sent to the {@code out} stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, File workingDirectory, OutputStream out) {
        return runProcess(command,workingDirectory,out,null,null,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output streams to use for the standard
     * out and the standard error, as well as the working directory to run the process in.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, File workingDirectory, OutputStream stdOut, OutputStream stdErr) {
        return runProcess(command,workingDirectory,stdOut,stdErr,null,false);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the input streams to use for the standard
     * in, as well as the working directory to run the process in. The standard out stream will be sent to {@code System.out},
     * as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, File workingDirectory, InputStream in) {
        return runProcess(command,workingDirectory,System.out,null,in,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output streams to use for the standard
     * out and the standard error, and the input stream to use for the standard input, as well as the working directory to
     * run the process in.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, File workingDirectory, OutputStream out, InputStream in) {
        return runProcess(command,workingDirectory,out,null,in,true);
    }

    /**
     * Run a process with the given command and wait for it to finish, specifying the output stream to use for the standard
     * out and the input stream to use for the standard input, as well as the working directory to run the process in.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the return value of the process.
     */
    public static int runProcess(List<String> command, File workingDirectory, OutputStream stdOut, OutputStream stdErr, InputStream in) {
        return runProcess(command,workingDirectory,stdOut,stdErr,in,false);
    }

        /**
     * Start a process with the given command. The standard out stream will be sent to {@code System.out},
     * as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command) {
        return runProcess(command,null,System.out,null,null,true,false);
    }

    /**
     * Start a process with the given command, specifying the output stream to use for the standard
     * out. The standard error stream will be sent to the {@code out} stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, OutputStream out) {
        return runProcess(command,null,out,null,null,true,false);
    }

    /**
     * Start a process with the given command, specifying the output streams to use for the standard
     * out and the standard error.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, OutputStream stdOut, OutputStream stdErr) {
        return runProcess(command,null,stdOut,stdErr,null,false,false);
    }

    /**
     * Start a process with the given command, specifying the input streams to use for the standard
     * in. The standard out stream will be sent to {@code System.out}, as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, InputStream in) {
        return runProcess(command,null,System.out,null,in,true,false);
    }

    /**
     * Start a process with the given command, specifying the output streams to use for the standard
     * out and the standard error, and the input stream to use for the standard input.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, OutputStream out, InputStream in) {
        return runProcess(command,null,out,null,in,true,false);
    }

    /**
     * Start a process with the given command, specifying the output stream to use for the standard
     * out and the input stream to use for the standard input.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, OutputStream stdOut, OutputStream stdErr, InputStream in) {
        return runProcess(command,null,stdOut,stdErr,in,false,false);
    }
    /**
     * Start a process with the given command, specifying the working directory to run the process
     * in. The standard out stream will be sent to {@code System.out}, as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, File workingDirectory) {
        return runProcess(command,workingDirectory,System.out,null,null,true,false);
    }

    /**
     * Start a process with the given command, specifying the output stream to use for the standard
     * out, as well as the working directory to run the process in. The standard error stream will be sent to the {@code out} stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, File workingDirectory, OutputStream out) {
        return runProcess(command,workingDirectory,out,null,null,true,false);
    }

    /**
     * Start a process with the given command, specifying the output streams to use for the standard
     * out and the standard error, as well as the working directory to run the process in.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, File workingDirectory, OutputStream stdOut, OutputStream stdErr) {
        return runProcess(command,workingDirectory,stdOut,stdErr,null,false,false);
    }

    /**
     * Start a process with the given command, specifying the input streams to use for the standard
     * in, as well as the working directory to run the process in. The standard out stream will be sent to {@code System.out},
     * as will the standard error stream.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, File workingDirectory, InputStream in) {
        return runProcess(command,workingDirectory,System.out,null,in,true,false);
    }

    /**
     * Start a process with the given command, specifying the output streams to use for the standard
     * out and the standard error, and the input stream to use for the standard input, as well as the working directory to
     * run the process in.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param out
     *        The stream to send the standard out (and error) to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, File workingDirectory, OutputStream out, InputStream in) {
        return runProcess(command,workingDirectory,out,null,in,true,false);
    }

    /**
     * Start a process with the given command, specifying the output stream to use for the standard
     * out and the input stream to use for the standard input, as well as the working directory to run the process in.
     *
     * @param command
     *        The program and arguments to run.
     *
     * @param workingDirectory
     *        The directory to run the process in.
     *
     * @param stdOut
     *        The stream to send the standard out to.
     *
     * @param stdErr
     *        The stream to send the standard error to.
     *
     * @param in
     *        The stream to send the standard input to.
     *
     * @return the process that was started.
     */
    public static Process startProcess(List<String> command, File workingDirectory, OutputStream stdOut, OutputStream stdErr, InputStream in) {
        return runProcess(command,workingDirectory,stdOut,stdErr,in,false,false);
    }

    private static int runProcess(List<String> command, File workingDirectory, OutputStream stdOut, OutputStream stdErr, InputStream stdIn, boolean combineOutStreams) {
        return runProcess(command,workingDirectory,stdOut,stdErr,stdIn,combineOutStreams,true).exitValue();
    }

    private static Thread processCleaner(final Process p, final List<Thread> threads, final List<Closeable> closeables,boolean daemon) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    p.waitFor();
                    for (Thread t : threads)
                        t.join();
                } catch (InterruptedException e) {
                    throw new RuntimeInterruptedException(e);
                } finally {
                    for (Closeable closeable : closeables)
                        closeResource(closeable);
                }
            }
        });
        t.setDaemon(daemon);
        t.start();
        return t;
    }

    private static Process runProcess(List<String> command, File workingDirectory, OutputStream stdOut, OutputStream stdErr, InputStream stdIn, boolean combineOutStreams, boolean wait) {
        ProcessBuilder pb = new ProcessBuilder(command);
        if (workingDirectory != null)
            pb.directory(workingDirectory);
        InputStream outInput;
        InputStream errInput = null;
        OutputStream inOutput = null;
        List<Thread> threads = new LinkedList<Thread>();
        pb.redirectErrorStream(combineOutStreams);
        try {
            Process p = pb.start();
            threads.add(redirectStream(outInput = p.getInputStream(),stdOut));
            if (stdErr != null)
                threads.add(redirectStream(errInput = p.getErrorStream(),stdErr));
            if (stdIn != null)
                redirectStream(stdIn,inOutput = new FlushingOutputStream(p.getOutputStream()));
            Thread cleanerThread = processCleaner(p,threads, Arrays.asList(outInput,errInput,inOutput),!wait);
            if (wait)
                cleanerThread.join();
            return p;
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeInterruptedException(e);
        }
    }

    private static Thread redirectStream(final InputStream in, final OutputStream out) {
        Thread t = new Thread() {
            public void run() {
                try {
                    int b;
                    while ((b = in.read()) != -1)
                        out.write(b);
                } catch (IOException e) {
                    throw new RuntimeIOException(e);
                }
            }
        };
        t.setDaemon(true);
        t.start();
        return t;
    }

    private static void closeResource(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            //swallow
        }
    }
}