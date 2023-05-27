# Repository parent for VW EKM modules

Every subdirectory is a separate maven-module and uses the parent pom in the root of the repository.

```
<modules>
		<module>vw-km-kernel</module>
		<module>vw-km-web</module>
		...
		<module>vw-km-delivery</module>
</modules>
```

## Repository Contents:
### Libraries and modules

* [vw-km-kernel](./vw-km-kernel/README.md)
* [vw-km-web](./vw-km-web/README.md)
* [process-designer](./process-designer/README.md)
* [vw-km-delivery](./vw-km-delivery/README.md)

## Plugin- and dependency-management.

Common used libraries should be configured in the parent pom.
All version numbers are configured in the properties section

```
<properties>
		<!-- Maven properties -->
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<java.version>8</java.version>
		...
		...
		<!-- plugin versions -->
		<maven-jar-plugin.version>3.2.0</maven-jar-plugin.version>
		...
		...
		<!-- 3rd party libraries -->
		<slf4j.version>1.7.7</slf4j.version>
		...
		...
		
</properties>
```

Very specific dependencies that are used by one project only should stay in the projects pom only and not in the parent pom.