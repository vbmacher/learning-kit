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

        Table::Table() : activeIndex(-1) {
        }

        Table::~Table() {
        }

        void Table::removeChild(ComponentsType::iterator component) {
            CompositeComponent::removeChild(component);
            long unsigned int size = children.size();
            if (size <= activeIndex) {
                activeIndex = size - 1;
            }
        }

        void Table::draw(Canvas &canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            canvas.drawRect(5, 5, width - 10, height - 10);
            
            // Middle dotted line
            Uint16 middle_x = (width - 5) / 2;
            canvas.dottedLine(middle_x, (Uint16)5, middle_x, (Uint16)(height - 10));
            
            CompositeComponent::draw(canvas);
        }

        void Table::move(Uint16 x, Uint16 y) {
            if (children.empty() || (activeIndex < 0)) {
                return;
            }
            children[activeIndex]->move(x, y);
        }

    }
}