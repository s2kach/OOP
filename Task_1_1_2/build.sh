#!/bin/bash

echo "=== Сборка проекта Blackjack ==="

echo "Очистка предыдущей сборки..."
rm -rf build

echo "Создание структуры директорий..."
mkdir -p build/classes build/docs build/jar

echo "Компиляция исходных кодов..."
find src/main/java -name "*.java" > sources.txt
javac -d build/classes @sources.txt
rm sources.txt

echo "Генерация документации..."
javadoc -d build/docs -sourcepath src/main/java -subpackages ru.nsu.dizmestev

echo "Создание JAR-файла..."
echo "Main-Class: ru.nsu.dizmestev.Main" > manifest.txt
jar -cfm build/jar/BlackJack.jar manifest.txt -C build/classes .
rm manifest.txt

echo "=== Сборка завершена ==="
echo "JAR-файл создан: build/jar/BlackJack.jar"
echo "Для запуска выполните: java -jar build/jar/BlackJack.jar"