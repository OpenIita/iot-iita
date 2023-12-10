/**
 * Copyright [2019-Present] [starBlues]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gitee.starblues.plugin.pack.prod;

import com.gitee.starblues.common.*;
import com.gitee.starblues.plugin.pack.RepackageMojo;
import com.gitee.starblues.plugin.pack.dev.DevRepackager;
import com.gitee.starblues.plugin.pack.utils.CommonUtils;
import com.gitee.starblues.utils.FilesUtils;
import com.gitee.starblues.utils.ObjectUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.gitee.starblues.common.PackageStructure.*;

/**
 * 文件夹包生成
 *
 * @author starBlues
 * @since 3.0.0
 * @version 3.1.1
 */
public class DirProdRepackager extends DevRepackager {

    protected final ProdConfig prodConfig;


    public DirProdRepackager(RepackageMojo repackageMojo, ProdConfig prodConfig) {
        super(repackageMojo);
        this.prodConfig = prodConfig;
    }

    @Override
    public void repackage() throws MojoExecutionException, MojoFailureException {
        super.repackage();
        try {
            resolveClasses();
            logSuccess();
        } catch (Exception e) {
            repackageMojo.getLog().error(e.getMessage(), e);
            throw new MojoFailureException(e);
        }
    }

    protected void logSuccess(){
        repackageMojo.getLog().info("Success package prod dir file : " + getRootDir());
    }

    @Override
    protected String createRootDir() throws MojoFailureException {
        String fileName = prodConfig.getFileName();
        String dirPath = FilesUtils.joiningFilePath(prodConfig.getOutputDirectory(), fileName);
        File dirFile = new File(dirPath);
        CommonUtils.deleteFile(dirFile);
        if(!dirFile.mkdirs()){
            throw new MojoFailureException("Create package dir failure: " + dirFile.getPath());
        }
        return dirFile.getPath();
    }

    @Override
    protected String getRelativeManifestPath() {
        return FilesUtils.joiningFilePath(META_INF_NAME, MANIFEST);
    }

    @Override
    protected String getRelativePluginMetaPath() {
        return FilesUtils.joiningFilePath(META_INF_NAME, PLUGIN_META_NAME);
    }

    @Override
    protected String getRelativeResourcesDefinePath() {
        return FilesUtils.joiningFilePath(META_INF_NAME, RESOURCES_DEFINE_NAME);
    }

    @Override
    protected Manifest getManifest() throws Exception {
        Manifest manifest = super.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.putValue(ManifestKey.PLUGIN_META_PATH, PROD_PLUGIN_META_PATH);
        attributes.putValue(ManifestKey.PLUGIN_PACKAGE_TYPE, PackageType.PLUGIN_PACKAGE_TYPE_DIR);
        return manifest;
    }

    @Override
    protected Properties createPluginMetaInfo() throws Exception {
        Properties properties = super.createPluginMetaInfo();
        properties.put(PluginDescriptorKey.PLUGIN_PATH, CLASSES_NAME);
        properties.put(PluginDescriptorKey.PLUGIN_RESOURCES_CONFIG, PROD_RESOURCES_DEFINE_PATH);
        String libDir = prodConfig.getLibDir();
        if(ObjectUtils.isEmpty(libDir)){
           libDir = Constants.RELATIVE_SIGN + PackageStructure.PROD_LIB_PATH;
        }
        properties.put(PluginDescriptorKey.PLUGIN_LIB_DIR, libDir);
        return properties;
    }

    protected void resolveClasses() throws Exception {
        String buildDir = repackageMojo.getProject().getBuild().getOutputDirectory();
        String path = FilesUtils.joiningFilePath(getRootDir(), CLASSES_NAME);
        File file = new File(path);
        FileUtils.forceMkdir(file);
        FileUtils.copyDirectory(new File(buildDir), file);
    }

    @Override
    protected Set<String> getDependenciesIndexSet() throws Exception {
        Set<Artifact> dependencies = repackageMojo.getFilterDependencies();
        String libDir = createLibDir();
        Set<String> dependencyIndexNames = new LinkedHashSet<>(dependencies.size());
        for (Artifact artifact : dependencies) {
            if(filterArtifact(artifact)){
                continue;
            }
            File artifactFile = artifact.getFile();
            FileUtils.copyFile(artifactFile, new File(FilesUtils.joiningFilePath(libDir, artifactFile.getName())));
            dependencyIndexNames.add(artifactFile.getName() + repackageMojo.resolveLoadToMain(artifact));
        }
        return dependencyIndexNames;
    }

    protected String createLibDir() throws IOException {
        String dir = FilesUtils.joiningFilePath(getRootDir(), PackageStructure.LIB_NAME);
        File file = new File(dir);
        if(file.mkdir()){
            return dir;
        }
        throw new IOException("Create " + PackageStructure.LIB_NAME + " dir failure");
    }

}
