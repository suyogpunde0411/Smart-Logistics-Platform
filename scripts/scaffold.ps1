$ErrorActionPreference = "Stop"

# Base paths
$backend = ".\backend"
$frontend = ".\frontend\src"
$docs = ".\docs"

# 1. Create root folders
New-Item -ItemType Directory -Force -Path $backend | Out-Null
New-Item -ItemType Directory -Force -Path ".\frontend" | Out-Null
New-Item -ItemType Directory -Force -Path $docs | Out-Null
New-Item -ItemType Directory -Force -Path ".\scripts" | Out-Null

# 2. Scaffold backend microservices
$services = @(
    "api-gateway", "service-registry", "config-server", "common-library",
    "user-service", "truck-service", "shipment-service", "matching-service",
    "tracking-service", "notification-service", "review-service", "admin-service",
    "analytics-service", "future-ai-service-placeholder"
)

$javaPackages = @(
    "config", "controller", "service", "repository", "entity", "dto", "mapper",
    "exception", "security", "validation", "util", "events", "client", "constants"
)

foreach ($svc in $services) {
    $svcPath = Join-Path $backend $svc
    $normalizedName = $svc.Replace("-", "")
    
    # Create Java package structure
    foreach ($pkg in $javaPackages) {
        $pkgPath = Join-Path $svcPath "src\main\java\com\smartlogistics\$normalizedName\$pkg"
        New-Item -ItemType Directory -Force -Path $pkgPath | Out-Null
    }
    
    # Create test folder
    $testPath = Join-Path $svcPath "src\test\java\com\smartlogistics\$normalizedName"
    New-Item -ItemType Directory -Force -Path $testPath | Out-Null
    
    # Create resources folder & application.yml
    $resPath = Join-Path $svcPath "src\main\resources"
    New-Item -ItemType Directory -Force -Path $resPath | Out-Null
    New-Item -ItemType File -Force -Path (Join-Path $resPath "application.yml") | Out-Null
    
    # Create README and pom.xml
    New-Item -ItemType File -Force -Path (Join-Path $svcPath "README.md") | Out-Null
    
    $pomContent = @"
<?xml version=`"1.0`" encoding=`"UTF-8`"?>
<project xmlns=`"http://maven.apache.org/POM/4.0.0`"
         xmlns:xsi=`"http://www.w3.org/2001/XMLSchema-instance`"
         xsi:schemaLocation=`"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd`">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.smartlogistics</groupId>
        <artifactId>smart-logistics-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>$svc</artifactId>
    <name>$svc</name>
    <description>Smart Logistics - $svc</description>

    <dependencies>
        <!-- Add service specific dependencies here -->
    </dependencies>
</project>
"@
    Set-Content -Path (Join-Path $svcPath "pom.xml") -Value $pomContent
}

# 3. Scaffold frontend
$frontendDirs = @(
    "assets", "components", "common", "layout", "pages", "hooks", "context",
    "redux", "services", "api", "types", "utils", "constants", "routes", "styles", "validators", "features"
)
foreach ($dir in $frontendDirs) {
    New-Item -ItemType Directory -Force -Path (Join-Path $frontend $dir) | Out-Null
}

$features = @(
    "Authentication", "Dashboard", "TruckManagement", "ShipmentManagement",
    "Matching", "Tracking", "Notifications", "Reviews", "Admin", "Analytics", "Profile"
)
$featureSubDirs = @("components", "pages", "hooks", "services", "types")

foreach ($feature in $features) {
    $featPath = Join-Path $frontend "features\$feature"
    foreach ($sub in $featureSubDirs) {
        New-Item -ItemType Directory -Force -Path (Join-Path $featPath $sub) | Out-Null
    }
}

# 4. Scaffold docs
$docFiles = @(
    "Architecture.md", "API-Guidelines.md", "Coding-Standards.md",
    "Database-Design.md", "Microservices.md", "Roadmap.md", "Folder-Structure.md"
)
foreach ($doc in $docFiles) {
    New-Item -ItemType File -Force -Path (Join-Path $docs $doc) | Out-Null
}

# 5. Create Parent POM
$parentPomContent = @"
<?xml version=`"1.0`" encoding=`"UTF-8`"?>
<project xmlns=`"http://maven.apache.org/POM/4.0.0`"
         xmlns:xsi=`"http://www.w3.org/2001/XMLSchema-instance`"
         xsi:schemaLocation=`"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd`">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.smartlogistics</groupId>
    <artifactId>smart-logistics-platform</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>api-gateway</module>
        <module>service-registry</module>
        <module>config-server</module>
        <module>common-library</module>
        <module>user-service</module>
        <module>truck-service</module>
        <module>shipment-service</module>
        <module>matching-service</module>
        <module>tracking-service</module>
        <module>notification-service</module>
        <module>review-service</module>
        <module>admin-service</module>
        <module>analytics-service</module>
        <module>future-ai-service-placeholder</module>
    </modules>

    <properties>
        <java.version>21</java.version>
        <spring-boot.version>3.2.4</spring-boot.version>
        <spring-cloud.version>2023.0.1</spring-cloud.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <lombok.version>1.18.30</lombok.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>`${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>`${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>`${lombok.version}</version>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>`${mapstruct.version}</version>
        </dependency>
    </dependencies>
</project>
"@
Set-Content -Path (Join-Path $backend "pom.xml") -Value $parentPomContent

Write-Host "Scaffolding complete!"
