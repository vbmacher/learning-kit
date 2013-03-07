/* 
 * File:   Table.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 14:16
 */

#include "table.h"
#include "canvas.h"

namespace github {
    namespace pong {

        Table::Table() {
        }

        Table::~Table() {
        }

        void Table::draw(Canvas &canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            canvas.drawRect(5, 5, width - 10, height - 10);
            
            // Middle dotted line
            Uint16 middle_x = (width - 5) / 2;
            canvas.dottedLine(middle_x, (Uint16) 5, middle_x, (Uint16) (height - 10));
        }
        
    }
}