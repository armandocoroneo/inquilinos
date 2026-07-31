#!/usr/bin/env sh
DIRname "$0"
CHMOD +x "$0"
exec java -Xmx64m -jar "$DIRname/gradle/wrapper/gradle-wrapper.jar" "$@"
