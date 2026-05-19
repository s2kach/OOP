include "tasks.groovy"
include "groups.groovy"

checkpoints {
    checkpoint name: "КТ 1", date: "20.10.2023"
    checkpoint name: "Зачет", date: "20.12.2023"
}

extraPoints student: "s2kach", task: "Task_1_1_1", points: 1.0

check {
    assign taskId: "Task_1_1_1", students: ["s2kach"]
    assign taskId: "Task_1_1_2", students: ["s2kach"]
    assign taskId: "Task_1_2_2", students: ["s2kach"]
}