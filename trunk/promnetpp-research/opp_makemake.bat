@echo off
set OMNETPP_HOME=C:\omnetpp-4.3
set PATH=%PATH%;%OMNETPP_HOME%\msys\bin
cmd /c %OMNETPP_HOME%\bin\opp_makemake.cmd
