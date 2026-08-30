package com.patrones.u2;

import java.util.Objects;

/**
 * Modelo de datos inmutable que representa la calificación de un estudiante en una asignatura.
 * Aplica principios de inmutabilidad garantizando seguridad de hilos y consistencia.
 * 
 * @author Alex Rodríguez
 */
public final class GradeRecord {

    private final String studentId;
    private final String studentName;
    private final String courseCode;
    private final double grade;

    /**
     * Constructor con validación de invariantes.
     *
     * @param studentId   Identificador único del estudiante (no nulo ni vacío)
     * @param studentName Nombre completo del estudiante (no nulo ni vacío)
     * @param courseCode  Código del curso/asignatura (no nulo ni vacío)
     * @param grade       Nota numérica obtenida (debe estar en rango válido 0.0 - 100.0)
     */
    public GradeRecord(String studentId, String studentName, String courseCode, double grade) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("studentId no puede ser nulo ni vacío");
        }
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("studentName no puede ser nulo ni vacío");
        }
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("courseCode no puede ser nulo ni vacío");
        }
        if (grade < 0.0 || grade > 100.0) {
            throw new IllegalArgumentException("grade debe estar en el rango de 0.0 a 100.0 (recibido: " + grade + ")");
        }

        this.studentId = studentId.trim();
        this.studentName = studentName.trim();
        this.courseCode = courseCode.trim();
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeRecord that = (GradeRecord) o;
        return Double.compare(that.grade, grade) == 0 &&
               Objects.equals(studentId, that.studentId) &&
               Objects.equals(studentName, that.studentName) &&
               Objects.equals(courseCode, that.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, studentName, courseCode, grade);
    }

    @Override
    public String toString() {
        return "GradeRecord{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", grade=" + grade +
                '}';
    }
}
