package com.pb.sawdust.meta.build;

import com.pb.sawdust.io.FileUtil;
import com.pb.sawdust.io.StreamConnector;
import com.pb.sawdust.io.ZipFile;

import java.io.*;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * The {@code SimpleJar} class provides a variety of convenience methods for dealing with jar files. Its main focus is
 * creating basic jar files (along with manifests) from already existing contents; that is, it does not deal with compiling
 * classes or whatnot (a task best left to a true build utility), but rather just grouping files together into a jar file.
 *
 * @author crf <br/>
 *         Started May 16, 2011 8:50:51 AM
 */
public class SimpleJar {

    /**
     * Create a simple jar by specifying the output file, the content root, the filter for the contents, and the manifest.
     *
     * @param outFile
     *        The full path of the jar to write.
     *
     * @param startPath
     *        The start path of the contents. Everything in this directory (including subdirectories and their contents)
     *        will be added, subtracting off this base path from their storage location in the jar file.
     *
     * @param filter
     *        The filter used to filter the contents. If a file/directory from {@code startPath} does not pass this filter,
     *        then it will not be added.
     *
     * @param manifest
     *        The manifest for the jar file, or {@code null} to use a default manifest.
     */
    public static void createSimpleJar(String outFile, String startPath, FileFilter filter, final Manifest manifest) {
        ZipFile zipFile = new ZipFile(outFile,true);
        File start = new File(startPath);
        for (File f : start.listFiles(filter))
            if (f.isDirectory())
                zipFile.addDirectory(new File(f.getName()),start,filter);
            else
                zipFile.addFile(new File(start,f.getName()),start);
        addManifestToZip(zipFile,manifest);
        zipFile.write();
    }

    /**
     * Create a simple jar by specifying the output file, the content root, and the filter for the contents. A default
     * manifest will be used.
     *
     * @param outFile
     *        The full path of the jar to write.
     *
     * @param startPath
     *        The start path of the contents. Everything in this directory (including subdirectories and their contents)
     *        will be added, subtracting off this base path from their storage location in the jar file.
     *
     * @param filter
     *        The filter used to filter the contents. If a file/directory from {@code startPath} does not pass this filter,
     *        then it will not be added.
     */
    public static void createSimpleJar(String outFile, String startPath, FileFilter filter) {
        createSimpleJar(outFile,startPath,filter,null);
    }

    /**
     * Create a simple jar by specifying the output file and the content root. A default
     * manifest will be used.
     *
     * @param outFile
     *        The full path of the jar to write.
     *
     * @param startPath
     *        The start path of the contents. Everything in this directory (including subdirectories and their contents)
     *        will be added, subtracting off this base path from their storage location in the jar file.
     */
    public static void createSimpleJar(String outFile, String startPath) {
        createSimpleJar(outFile,startPath, FileUtil.getTransparentFileFilter());
    }

    /**
     * Get a copy of a manifest, with some additional attributes. If the manifest passed to this method is {@code null},
     * then a new one will be created. The added attributes are:
     * <ul>
     *     <li>
     *         Created-By - the creator of the jar.
     *     </li>
     *     <li>
     *         Build-Date - the date this jar was created (sets this as the date/time when this method is called).
     *     </li>
     *     <li>
     *         Build-Java-Version - the version of java used to build the jar.
     *     </li>
     * </ul>
     *
     * @param manifest
     *        The manifest to copy, or {@code null} if a new (empty) one is to be created.
     *
     * @param createdBy
     *        The name to use for the "Created-By" attribute.
     *
     * @return a manifest with the additional attributes.
     */
    public static Manifest getBasicManifest(Manifest manifest, String createdBy) {
        Manifest sm = manifest == null ? new Manifest() : new Manifest(manifest);
        Attributes.Name cb = new Attributes.Name("Created-By");
        Attributes att = sm.getMainAttributes();
        att.put(cb,att.containsKey(cb) ? att.get(cb) + ";" + createdBy : createdBy);
        att.put(new Attributes.Name("Build-Date"),new Date().toString());
        att.put(new Attributes.Name("Build-Java-Version"),System.getProperty("java.version"));
        if (!att.containsKey(Attributes.Name.MANIFEST_VERSION))
            att.put(Attributes.Name.MANIFEST_VERSION,"1.0");
        return sm;
    }

    private static Manifest standardManifest(Manifest manifest) {
        return getBasicManifest(manifest,"SimpleJar");
    }

    /**
     * Convenience utility to combine multiple jar files into one. Naming overlaps between jars are not noted, so this
     * method should not be used when such a situation may arise.
     *
     * @param outFile
     *        The full path to the output jar file.
     *
     * @param manifest
     *        The manifest to use for the combined jar (manifests from the component jars are ignored).
     *
     * @param jars
     *        The jar files to combine.
     */
    public static void combineJars(String outFile, Manifest manifest, String ... jars) {
        String manifestEntry = "META-INF/MANIFEST.MF";
        ZipFile zipFile = new ZipFile(outFile,true);
        for (String jar : jars) {
            final ZipFile jarz = new ZipFile(jar);
            for (String entry : jarz) {
                if (entry.equalsIgnoreCase(manifestEntry))
                    continue;
                if (entry.endsWith("/")) { //directory
                    zipFile.addDirectoryByName(entry);
                } else {
                    final String e = entry;
                    zipFile.addEntry(entry,new ZipFile.ZipEntrySource() {
                        InputStream is = null;

                        public void writeData(OutputStream os) throws IOException {
                            is = jarz.getInputStream(e);
                            StreamConnector.connectStreams(is,os);
                        }

                        public void close() throws IOException {
                            if (is != null)
                                is.close();
                        }
                    });
                }
            }
        }
        addManifestToZip(zipFile,manifest);
        zipFile.write();
    }

    /**
     * Convenience utility to combine multiple jar files into one. Naming overlaps between jars are not noted, so this
     * method should not be used when such a situation may arise. The different manifests in the component jars will be
     * combined, and any conflicts will be returned in a mapping from the attribute key to a list of the various values
     * found for it.
     *
     * @param outFile
     *        The full path to the output jar file.
     *
     * @param jars
     *        The jar files to combine.
     *
     * @return a mapping listing any manifest conflicts that were found.
     */
    public static Map<Object,Set<Object>> combineJars(String outFile, String ... jars) {
        Map<Object,Set<Object>> manifestConflicts = new HashMap<Object,Set<Object>>();
        combineJars(outFile, standardManifest(combineManifests(manifestConflicts,jars)),jars);
        return manifestConflicts;
    }

    private static void addManifestToZip(ZipFile zipFile, final Manifest manifest) {
        zipFile.addDirectoryByName("META-INF");
        zipFile.addEntry("META-INF/MANIFEST.MF",new ZipFile.ZipEntrySource() {
            public void writeData(OutputStream os) throws IOException {
                standardManifest(manifest).write(os);
            }

            public void close() throws IOException { }
        });
    }

    /**
     * Combine the manifests in a series of jar files into one. Any conflicts will be stored in the passed map which provides
     * a mapping from the manifest key to the list of different values found in the various manifests.
     *
     * @param conflicts
     *        The map to put the conflicts in.
     *
     * @param jars
     *        The jar files holding the source manifests.
     *
     * @return a manifests merging all of the source manifests.
     */
    public static Manifest combineManifests(Map<Object,Set<Object>> conflicts, String ... jars) {
        List<Manifest> manifests = new LinkedList<Manifest>();
        try {
            for (String jar : jars) {
                JarFile jf = new JarFile(jar);
                Manifest manifest = jf.getManifest();
                if (manifest != null)
                    manifests.add(manifest);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return combineManifests(conflicts,manifests.toArray(new Manifest[manifests.size()]));
    }

    private static Manifest combineManifests(Map<Object,Set<Object>> conflicts, Manifest ... manifests) {
        Manifest man = new Manifest();
        Attributes main = man.getMainAttributes();
        main.put(Attributes.Name.MANIFEST_VERSION,"1.0");
        Map<String,Attributes> atts = man.getEntries();
        for (Manifest manifest : manifests) {
            combineAttributes(manifest.getMainAttributes(),main,conflicts);
            Map<String,Attributes> entries = manifest.getEntries();
            for (String ek : entries.keySet()) {
                if (!atts.containsKey(ek))
                    atts.put(ek,new Attributes(entries.get(ek)));
                else
                    combineAttributes(entries.get(ek),atts.get(ek),conflicts);
            }
        }
        return man;
    }

    private static void combineAttributes(Attributes from, Attributes to, Map<Object,Set<Object>> conflicts) {
        for (Map.Entry<Object,Object> entry : from.entrySet()) {
            if (conflicts != null && to.containsKey(entry.getKey()) && !to.get(entry.getKey()).equals(entry.getValue())) {
                if (!conflicts.containsKey(entry.getKey())) {
                    conflicts.put(entry.getKey(), new HashSet<Object>());
                    conflicts.get(entry.getKey()).add(to.get(entry.getKey()));
                }
                conflicts.get(entry.getKey()).add(entry.getValue());
            }
            to.put(entry.getKey(),entry.getValue());
        }
    }

    public static void main(String ... args) {
        String clsUri = SimpleJar.class.getName().replace('.','/') + ".class";
        String clsPath = SimpleJar.class.getClassLoader().getResource(clsUri).getPath();
        clsPath = new File(clsPath.substring(0,clsPath.length()-clsUri.length())).getParentFile().getPath();
        FileFilter ff = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.getName().equals("test");
            }
        };
        createSimpleJar("d:/dump/teste.jar",clsPath,ff);
        System.out.println(combineJars("d:/dump/testes.jar","d:/dump/teste.jar","D:\\code\\work\\java\\sawdust\\lib\\jblas-1.2.0.jar","D:\\code\\work\\java\\sawdust\\lib\\h2-1.3.148.jar"));

//        new SlimJim().entry("-d","com.pb.sawdust.cookbook.az_statewide.LdtPrep");
    }
}