package net.glowstone;

import com.google.common.io.PatternFilenameFilter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GlowPluginTypeDetector {

    public List<URL> bukkitPluginURLs = new ArrayList<>();
    public List<URL> spongePluginURLs = new ArrayList<>();
    public List<URL> canaryPluginURLs = new ArrayList<>();
    public List<URL> unrecognizedPluginURLs = new ArrayList<>();

    private File directory;
    private Logger logger;

    public GlowPluginTypeDetector(File directory, Logger logger) {
        this.directory = directory;
        this.logger = logger;
    }

    public void scan() {
        File[] files = directory.listFiles(new PatternFilenameFilter(".+\\.jar"));
        if (files == null || files.length == 0) {
            return;
        }

        List<URL> urls = new ArrayList<>(files.length);
        for (File jar : files) {
            try {
                urls.add(jar.toURI().toURL());
            } catch (MalformedURLException e) {
                logger.log(Level.WARNING, "PluginTypeDetector: Malformed URL: " + jar, e);
            }
        }

        for (URL url : urls) {
            scanURL(url);
        }

        logger.info("PluginTypeDetector: found " +
                bukkitPluginURLs.size() + " Bukkit, " +
                spongePluginURLs.size() + " Sponge, " +
                canaryPluginURLs.size() + " Canary, " +
                unrecognizedPluginURLs.size() + " unknown plugins (scanned " + urls.size() + ")");
        if (unrecognizedPluginURLs.size() != 0) {
            for (URL url : unrecognizedPluginURLs) {
                logger.warning("Unrecognized plugin: " + url);
            }
        }
    }

    private void scanURL(URL url) {
        boolean isBukkit = false;
        boolean isSponge = false;
        boolean isCanary = false;

        URLClassLoader root = new URLClassLoader(new URL[]{url}, GlowPluginTypeDetector.class.getClassLoader());
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, root);

        try (InputStream fileIn = url.openStream();
             ZipInputStream zipIn = new ZipInputStream(fileIn)
        ) {
            ZipEntry entryIn;
            while ((entryIn = zipIn.getNextEntry()) != null) {
                String name = entryIn.getName();

                if (name.equals("plugin.yml")) {
                    isBukkit = true;
                }

                if (name.equals("Canary.inf")) {
                    isCanary = true;
                }

                if (name.endsWith(".class") && !entryIn.isDirectory()) {
                    name = name.substring(0, name.length() - 6).replace("/", ".");

                    Class<?> clazz;
                    try {
                        clazz = classLoader.loadClass(name); // TODO: possible to check annotation with 'loading'?
                    } catch (Throwable t) {
                        logger.log(Level.WARNING, "PluginTypeDetector: Error loading " + url.getFile() + "/" + name, t);
                        continue;
                    }

                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.toString().startsWith("@org.spongepowered.api.plugin.Plugin")) {
                            isSponge = true;
                        }
                        // TODO: net.minecraftforge.fml.common.Mod, cpw.mods.fml.common.Mod for isForge/FML
                        //System.out.println("ann: " + annotation.toString());
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "PluginTypeDetector: Error reading " + url, ex);
        }

        if (isBukkit) bukkitPluginURLs.add(url);
        if (isSponge) spongePluginURLs.add(url);
        if (isCanary) canaryPluginURLs.add(url);

        if (!isBukkit && !isSponge && !isCanary) unrecognizedPluginURLs.add(url);
    }
}
