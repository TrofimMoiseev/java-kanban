# java-kanban
Трекер задач:
Трекеры задач позволяют эффективно организовать совместную работу над задачами. 

Простейший кирпичик трекера — задача (англ. task). У неё есть следующие свойства:
Название, кратко описывающее суть задачи (например, «Переезд»).
Описание, в котором раскрываются детали.
Уникальный идентификационный номер задачи, по которому её можно будет найти.
Статус, отображающий её прогрессЖ
  1. NEW — задача только создана, но к её выполнению ещё не приступили.
  2. IN_PROGRESS — над задачей ведётся работа.
  3. DONE — задача выполнена.
   
   Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask).
Большая задача, которая делится на подзадачи, называется эпиком (англ. epic).

   Идентификатор задачи:
   В трекере у каждого типа задач есть идентификатор. Это целое число, уникальное для всех типов задач. 
По нему находят, обновляют, удаляют задачи. При создании задачи менеджер присваивает ей новый идентификатор.
   
   Менеджер:
В нём следующие функции:
-Возможность хранить задачи всех типов. 
-Методы для каждого из типа задач
* Получение списка всех задач.
* Удаление всех задач.
* Получение по идентификатору.
* Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
* Удаление по идентификатору.
-Дополнительные методы:
* Получение списка всех подзадач определённого эпика.
~Управление статусами осуществляется по следующему правилу:
* Менеджер сам не выбирает статус для задачи. 
Информация о нём приходит менеджеру вместе с информацией о самой задаче.
По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
* Для эпиков:
   Если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
   Если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
   Во всех остальных случаях статус должен быть IN_PROGRESS.
