
    $releaseVersion=1.0.1 
    mvn clean versions:set -DnewVersion="$releaseVersion" -DgenerateBackupPoms=false

also update versions on modules 

    mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} versions:commit

the same as above

    mvn release:update-versions