package org.signalml.app.worker.processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs a process and notifies the {@link ProcessManager} when it ends.
 *
 * @author Tomasz Sawicki
 */
public class ProcessSubject extends Thread {

        /**
         * {@link ProcessBuilder} that will start the process.
         */
        private ProcessBuilder processBuilder;

        /**
         * {@link Process} object.
         */
        private Process process = null;

        /**
         * {@link ProcessManager} to notify when the process ends.
         */
        private ProcessManager processManager;

        /**
         * Process id.
         */
        private String id;

        /**
         * Process delay.
         */
        private long delay;

        /**
         * Constructor prepares the process.
         *
         * @param id the process id
         * @param path path to the executable file
         * @param parameters command line parameters
         */
        public ProcessSubject(String id, String path, List<String> parameters, ProcessManager processManager) {

                File executable = new File(path);
                ArrayList<String> command = new ArrayList<String>();
                command.add("./" + executable.getName());
                command.addAll(parameters);
                String directory = executable.getAbsolutePath().substring(0, executable.getAbsolutePath().length() - executable.getName().length());

                this.id = id;
                this.processManager = processManager;
                this.processBuilder = new ProcessBuilder(command);
                this.processBuilder.directory(new File(directory));
        }

        /**
         * Sets the delay.
         *
         * @param delay the delay
         */
        public void setDelay(long delay) {

                this.delay = delay;
        }

        /**
         * Gets the process.
         *
         * @return the process
         */
        public Process getProcess() {

                return process;
        }

        /**
         * Starts the process with a given delay
         */
        public void startProcess() {

                this.start();
        }

        /**
         * Kills the process.
         */
        public void killProcess() {

                this.interrupt();
        }

        /**
         * Sleeps for {@link #delay} miliseconds, then starts the process,
         * and waits for it to finish. The {@link #processManager} is notified
         * again afterwards.
         */
        @Override
        public void run() {

                try {
                        Thread.sleep(delay);

                        try {
                                process = processBuilder.start();
                        } catch (IOException ex) {
                                Logger.getLogger(ProcessSubject.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        process.waitFor();
                        processManager.processEnded(id, process.exitValue());

                } catch (InterruptedException ex) {

                        if (process != null) process.destroy();
                }
        }
}