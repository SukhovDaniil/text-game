# Текстовая игра

## Идея игры

???

## Наброски

* Персонаж
* Рюкзак персонажа
* Артефакты в рюкзаке
* Свойства артефактов
* NPC (5 штук)
* Задания от NPC
* Пользовательский интерфейс: tg-bot

## Идеи

* Внутриигровая карта (визуальное отображение недоступно пользователю)

## Версии

* jvm - liberic 17

Что нужно сделать?

* Нужно разделить используемые объекты потоками
* нужно причесать остановку потоков
* нужно научится с ними работать вне контекста потоков (из бота взаимодействовать с NPC в потоках)

попробовать испаользовать

java.util.concurrent.Executors java.util.concurrent.ForkJoinPool

Что ещё сделать:

- Инициировать действия из чата ТГ с NPC, который в потоке двигается по карте
