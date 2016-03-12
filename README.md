# htmlres-maven-plugin
Maven Plugin which automatically generates HTML files with resource links

Usage example:

    <plugins>
        <plugin>
            <groupId>com.github.acc15</groupId>
            <artifactId>htmlres-maven-plugin</artifactId>
            <version>${project.version}</version>
            <configuration>
                <template>src/test/resources/index.html</template>
                <jsResources>
                    <jsResource><url>webjars/angularjs/angular.js</url></jsResource>
                </jsResources>
                <groups>
                    <group>
                        <targetFile>target/index.html</targetFile>
                        <jsResources>
                            <jsResource><url>js/app.js</url></jsResource>
                            <jsResource>
                                <dir>src/test/resources</dir>
                                <includes>
                                    <include>js/**/*.js</include>
                                </includes>
                            </jsResource>
                        </jsResources>
                        <cssResources>
                            <cssResource>
                                <dir>src/test/resources</dir>
                                <includes>
                                    <include>css/**/*.css</include>
                                </includes>
                            </cssResource>
                        </cssResources>
                    </group>
                    <group>
                        <useMinified>true</useMinified>
                        <targetFile>target/index.min.html</targetFile>
                        <jsResources>
                            <jsResource><url>all.js</url></jsResource>
                        </jsResources>
                        <cssResources>
                            <cssResource><url>all.css</url></cssResource>
                        </cssResources>
                    </group>
                </groups>
            </configuration>
        </plugin>
    </plugins>

This will generate two html files `target/index.html` and `target/index.min.html` with
all required javascript and css urls.

`index.min.html` will contain urls to minified files