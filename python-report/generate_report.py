import sys
import json
import os
from docxtpl import DocxTemplate

def generate_report(data, output_path="운동크루보고서.docx"):
    base_dir = os.path.dirname(os.path.abspath(__file__))
    template_path = os.path.join(base_dir, "format.docx")

    doc = DocxTemplate(template_path)
    doc.render(data)
    doc.save(os.path.join(base_dir, output_path))
    print(f"✅ 보고서 생성 완료: {output_path}")

if __name__ == "__main__":
    input_json = sys.stdin.read()
    data = json.loads(input_json)
    generate_report(data)
