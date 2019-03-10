import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = "com.lhrsite.xshop.po;"
typeMapping = [
        (~/(?i)int/)                         : "Integer",
        (~/(?i)float|double|real/)           : "double",
        (~/(?i)decimal/)                     : "BigDecimal",
        (~/(?i)datetime|date|time|timestamp/): "Date",
        (~/(?i)/)                            : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable }.each { generate(it, dir) }
}

def getPackageName(dir) {
    return dir.toString().replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

def generate(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    packageName = getPackageName(dir)
    new File(dir, className + ".java").withPrintWriter { out -> generate(out, className, fields) }
}

def generate(out, className, fields) {
    out.println "package $packageName"
    out.println ""
    out.println "import java.util.Date;"
    out.println "import java.io.Serializable;"
    out.println "import lombok.Data;"
    out.println "import javax.persistence.*;"
    out.println "import java.math.BigDecimal;"
    out.println ""
    out.println "@Entity"
    out.println "@Data"
    out.println "public class $className implements Serializable{"
    out.println ""
    fields.each() {
        if (it.annos != "") out.println "  ${it.annos}"
        if (it.commoent != "") out.println "    /* ${it.commoent} */"
        out.println "    private ${it.type} ${it.name};"
    }
    out.println ""
//  fields.each() {
//    out.println ""
//    out.println "  public ${it.type} get${it.name.capitalize()}() {"
//    out.println "    return ${it.name};"
//    out.println "  }"
//    out.println ""
//    out.println "  public void set${it.name.capitalize()}(${it.type} ${it.name}) {"
//    out.println "    this.${it.name} = ${it.name};"
//    out.println "  }"
//    out.println ""
//  }
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           colName : col.getName(),
                           name    : javaName(col.getName(), false),
                           type    : typeStr,
                           commoent: col.getComment(),
                           annos   : ""]]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
