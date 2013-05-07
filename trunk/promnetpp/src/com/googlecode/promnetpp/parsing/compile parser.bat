@echo off
set PATH=%PATH%;C:\javacc-5.0\bin
call jjtree Parser.jjt
call javacc Parser.jj
pause
