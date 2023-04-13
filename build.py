import shutil
import os
import datetime
import subprocess

def pr(ms):
    print("[LBD] -> " + ms)

VERSION = datetime.datetime.now().strftime("%d.%m.%Y %H:%M")

print("[ Laefye's Build Tool ]")
pr("PluginCinema.java -> * PluginCinema.java.original")
shutil.copy("src/main/java/com/github/laefye/plugincinema/PluginCinema.java", "src/main/java/com/github/laefye/plugincinema/PluginCinema.java.original")
pr("PluginCinema.java <- {{VERSION}} (%s)" % VERSION)
with open("src/main/java/com/github/laefye/plugincinema/PluginCinema.java", "w") as o:
    with open("src/main/java/com/github/laefye/plugincinema/PluginCinema.java.original", "r") as i:
        o.write(i.read().replace("{{VERSION}}", VERSION))
pr("> gradle")
subprocess.run(['gradlew.bat', 'build'])
pr("PluginCinema.java * <- PluginCinema.java.original")
shutil.copy("src/main/java/com/github/laefye/plugincinema/PluginCinema.java.original", "src/main/java/com/github/laefye/plugincinema/PluginCinema.java")
pr("X | PluginCinema.java.original")
os.unlink("src/main/java/com/github/laefye/plugincinema/PluginCinema.java.original")

pr("Output: " + os.path.abspath('build/libs'))