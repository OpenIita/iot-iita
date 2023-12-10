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

package com.gitee.starblues.plugin.pack.main;

import com.gitee.starblues.common.PackageStructure;
import com.gitee.starblues.common.PackageType;
import com.gitee.starblues.plugin.pack.utils.CommonUtils;
import com.gitee.starblues.utils.FilesUtils;
import com.gitee.starblues.utils.ObjectUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.gitee.starblues.common.ManifestKey.*;

/**
 * jar 外置包
 *
 * @author starBlues
 * @since 3.0.0
 * @version 3.0.2
 */
public class JarOuterPackager extends JarNestPackager {

    private static final String LIB_INDEXES_SPLIT = " ";

    private final Set<String> dependencyIndexNames = new LinkedHashSet<>();

    public JarOuterPackager(MainRepackager mainRepackager) {
        super(mainRepackager);
    }

    @Override
    public void repackage() throws MojoExecutionException, MojoFailureException {
        // 生成依赖文件夹
        String rootDir = createRootDir();
        mainConfig.setOutputDirectory(rootDir);
        super.repackage();
    }

    @Override
    protected void writeClasses() throws Exception {
        String buildDir = repackageMojo.getProject().getBuild().getOutputDirectory();
        packageJar.copyDirToPackage(new File(buildDir), null);
    }

    private String createRootDir() throws MojoFailureException{
        String outputDirectory = mainConfig.getOutputDirectory();
        String fileName = mainConfig.getFileName();
        String rootDirPath = FilesUtils.joiningFilePath(outputDirectory, fileName);
        File rootFile = new File(rootDirPath);
        CommonUtils.deleteFile(rootFile);
        if(rootFile.mkdirs()){
            return rootDirPath;
        } else {
            throw new MojoFailureException("Create dir failure : " + rootDirPath);
        }
    }

    @Override
    protected Manifest getManifest() throws Exception {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.putValue(MANIFEST_VERSION, MANIFEST_VERSION_1_0);
        attributes.putValue(START_CLASS, mainConfig.getMainClass());
        attributes.putValue(MAIN_CLASS, MAIN_CLASS_VALUE);
        attributes.putValue(MAIN_PACKAGE_TYPE, PackageType.MAIN_PACKAGE_TYPE_JAR_OUTER);
        attributes.putValue(MAIN_LIB_DIR, getLibPath());
        attributes.putValue(DEVELOPMENT_MODE, mainConfig.getDevelopmentMode());

        // 增加jar包title和version属性
        MavenProject mavenProject = this.repackageMojo.getProject();
        attributes.putValue(IMPLEMENTATION_TITLE, mavenProject.getArtifactId());
        attributes.putValue(IMPLEMENTATION_VERSION, mavenProject.getVersion());
        return manifest;
    }


    private String getLibIndexes() throws Exception {
        if(dependencyIndexNames.isEmpty()){
            return "";
        }
        StringBuilder libName = new StringBuilder();
        for (String dependencyIndexName : dependencyIndexNames) {
            libName.append(dependencyIndexName).append(LIB_INDEXES_SPLIT);
        }
        return libName.toString();
    }

    @Override
    protected void writeDependencies() throws Exception {
        Set<Artifact> dependencies = repackageMojo.getSourceDependencies();
        for (Artifact artifact : dependencies) {
            if(filterArtifact(artifact)){
                continue;
            }
            if(CommonUtils.isPluginFrameworkLoader(artifact)){
                // 本框架loader依赖
                packageJar.copyZipToPackage(artifact.getFile());
            } else {
                File artifactFile = artifact.getFile();
                String libPath = getLibPath();
                if(FilesUtils.isRelativePath(libPath)){
                    libPath = FilesUtils.resolveRelativePath(mainConfig.getOutputDirectory(), getLibPath());
                } else {
                    libPath = FilesUtils.joiningFilePath(mainConfig.getOutputDirectory(), libPath);
                }
                String targetFilePath = FilesUtils.joiningFilePath(libPath, artifactFile.getName());
                FileUtils.copyFile(artifactFile, new File(targetFilePath));
                dependencyIndexNames.add(artifactFile.getName());
            }
        }
    }

    private String getLibPath(){
        String libDir = PackageStructure.LIB_NAME;
        if(!ObjectUtils.isEmpty(mainConfig.getLibDir())){
            libDir = mainConfig.getLibDir();
        }
        return libDir;
    }

}
